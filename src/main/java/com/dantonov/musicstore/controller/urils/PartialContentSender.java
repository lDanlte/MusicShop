package com.dantonov.musicstore.controller.urils;

import com.mongodb.gridfs.GridFSDBFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author denis.antonov
 * @since 26.03.17.
 */
public class PartialContentSender {

    private static final Logger logger = LoggerFactory.getLogger(PartialContentSender.class);

    private static final int DEFAULT_BUFFER_SIZE = 128 << 10;
    private static final long DEFAULT_EXPIRE_TIME = TimeUnit.DAYS.toMillis(1);
    private static final String MULTIPART_BOUNDARY = "MULTIPART_BYTERANGES";

    private final InputStream is;
    private final String fileName;
    private final long lastModified;
    private final long fileLength;
    private String mimeType;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private PartialContentSender(final InputStream is, final String fileName, final long lastModified, final long fileLength) {
        this.is = is;
        this.fileName = fileName;
        this.lastModified = lastModified;
        this.fileLength = fileLength;
    }

    public static PartialContentSender fromGridFSDBFile(final GridFSDBFile file) {
        return new PartialContentSender(file.getInputStream(), file.getId().toString(), file.getUploadDate().getTime(), file.getLength());
    }

    public PartialContentSender withMimeType(final String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public PartialContentSender withRequest(final HttpServletRequest httpRequest) {
        this.request = httpRequest;
        return this;
    }

    public PartialContentSender withResponse(final HttpServletResponse httpResponse) {
        this.response = httpResponse;
        return this;
    }

    public void serveResource() throws Exception {
        if (response == null || request == null) {
            return;
        }

        final String ifNoneMatch = request.getHeader("If-None-Match");
        if (ifNoneMatch != null && HttpUtils.matches(ifNoneMatch, fileName)) {
            response.setHeader("ETag", fileName);
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        final long ifModifiedSince = request.getDateHeader("If-Modified-Since");
        if (ifNoneMatch == null && ifModifiedSince != -1 && ifModifiedSince + 1000 > lastModified) {
            response.setHeader("ETag", fileName);
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        }

        final String ifMatch = request.getHeader("If-Match");
        if (ifMatch != null && !HttpUtils.matches(ifMatch, fileName)) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }

        final long ifUnmodifiedSince = request.getDateHeader("If-Unmodified-Since");
        if (ifUnmodifiedSince != -1 && ifUnmodifiedSince + 1000 <= lastModified) {
            response.sendError(HttpServletResponse.SC_PRECONDITION_FAILED);
            return;
        }

        final Range full = new Range(0, fileLength - 1, fileLength);
        final List<Range> ranges = new ArrayList<>();

        final String range = request.getHeader("Range");
        if (range != null) {

            // Range header format "bytes=n-n,n-n,n-n...".
            if (!range.matches("^bytes=\\d*-\\d*(,\\d*-\\d*)*$")) {
                response.setHeader("Content-Range", "bytes */" + fileLength);
                response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                return;
            }

            final String ifRange = request.getHeader("If-Range");
            if (ifRange != null && !ifRange.equals(fileName)) {
                try {
                    final long ifRangeTime = request.getDateHeader("If-Range");
                    if (ifRangeTime != -1) {
                        ranges.add(full);
                    }
                } catch (IllegalArgumentException ignore) {
                    ranges.add(full);
                }
            }

            if (ranges.isEmpty()) {
                for (final String part : range.substring(6).split(",")) {
                    long start = Range.sublong(part, 0, part.indexOf("-"));
                    long end = Range.sublong(part, part.indexOf("-") + 1, part.length());

                    if (start == -1) {
                        start = fileLength - end;
                        end = fileLength - 1;
                    } else if (end == -1 || end > fileLength - 1) {
                        end = fileLength - 1;
                    }

                    if (start > end) {
                        response.setHeader("Content-Range", "bytes */" + fileLength);
                        response.sendError(HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE);
                        return;
                    }
                    ranges.add(new Range(start, end, fileLength));
                }
            }
        }

        String disposition = "inline";

        if (mimeType == null) {
            mimeType = "application/octet-stream";
        } else if (!mimeType.startsWith("image")) {
            // Else, expect for images, determine content disposition. If content type is supported by
            // the browser, then set to inline, else attachment which will pop a 'save as' dialogue.
            final String accept = request.getHeader("Accept");
            disposition = accept != null && HttpUtils.accepts(accept, mimeType) ? "inline" : "attachment";
        }
        logger.debug("Content-Type : {}", mimeType);
        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setHeader("Content-Type", mimeType);
        response.setHeader("Content-Disposition", disposition + ";filename=\"" + fileName + "\"");
        logger.debug("Content-Disposition : {}", disposition);
        response.setHeader("Accept-Ranges", "bytes");
        response.setHeader("ETag", fileName);
        response.setDateHeader("Last-Modified", lastModified);
        response.setDateHeader("Expires", System.currentTimeMillis() + DEFAULT_EXPIRE_TIME);

        try (InputStream input = new BufferedInputStream(is);
             OutputStream output = response.getOutputStream()) {

            if (ranges.isEmpty() || ranges.get(0) == full) {
                logger.info("Return full file");
                response.setContentType(mimeType);
                response.setHeader("Content-Range", "bytes " + full.start + "-" + full.end + "/" + full.total);
                response.setHeader("Content-Length", String.valueOf(full.length));
                Range.copy(input, output, fileLength, full.start, full.length);

            } else if (ranges.size() == 1) {
                final Range r = ranges.get(0);
                logger.info("Return 1 part of file : from ({}) to ({})", r.start, r.end);
                response.setContentType(mimeType);
                response.setHeader("Content-Range", "bytes " + r.start + "-" + r.end + "/" + r.total);
                response.setHeader("Content-Length", String.valueOf(r.length));
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                Range.copy(input, output, fileLength, r.start, r.length);

            } else {

                response.setContentType("multipart/byteranges; boundary=" + MULTIPART_BOUNDARY);
                response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);

                final ServletOutputStream sos = (ServletOutputStream) output;

                for (final Range r : ranges) {
                    logger.info("Return multi part of file : from ({}) to ({})", r.start, r.end);
                    sos.println();
                    sos.println("--" + MULTIPART_BOUNDARY);
                    sos.println("Content-Type: " + mimeType);
                    sos.println("Content-Range: bytes " + r.start + "-" + r.end + "/" + r.total);
                    Range.copy(input, output, fileLength, r.start, r.length);
                }
                sos.println();
                sos.println("--" + MULTIPART_BOUNDARY + "--");
            }
        }

    }

    private static class Range {
        long start;
        long end;
        long length;
        long total;

        Range(final long start, final long end, final long total) {
            this.start = start;
            this.end = end;
            this.length = end - start + 1;
            this.total = total;
        }

        static long sublong(final String value, final int beginIndex, final int endIndex) {
            final String substring = value.substring(beginIndex, endIndex);
            return (substring.length() > 0) ? Long.parseLong(substring) : -1;
        }

        private static void copy(final InputStream input, final OutputStream output, final long inputSize, final long start, final long length) throws IOException {
            final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int read;

            if (inputSize == length) {
                while ((read = input.read(buffer)) > 0) {
                    output.write(buffer, 0, read);
                    output.flush();
                }
            } else {
                input.skip(start);
                long toRead = length;

                while ((read = input.read(buffer)) > 0) {
                    if ((toRead -= read) > 0) {
                        output.write(buffer, 0, read);
                        output.flush();
                    } else {
                        output.write(buffer, 0, (int) toRead + read);
                        output.flush();
                        break;
                    }
                }
            }
        }
    }

    private static class HttpUtils {

        static boolean accepts(final String acceptHeader, final String toAccept) {
            final String[] acceptValues = acceptHeader.split("\\s*(,|;)\\s*");
            Arrays.sort(acceptValues);

            return Arrays.binarySearch(acceptValues, toAccept) > -1
                    || Arrays.binarySearch(acceptValues, toAccept.replaceAll("/.*$", "/*")) > -1
                    || Arrays.binarySearch(acceptValues, "*/*") > -1;
        }


        static boolean matches(final String matchHeader, final String toMatch) {
            final String[] matchValues = matchHeader.split("\\s*,\\s*");
            Arrays.sort(matchValues);
            return Arrays.binarySearch(matchValues, toMatch) > -1
                    || Arrays.binarySearch(matchValues, "*") > -1;
        }
    }

}
