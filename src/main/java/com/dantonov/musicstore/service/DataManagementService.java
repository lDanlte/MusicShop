package com.dantonov.musicstore.service;

import com.dantonov.musicstore.exception.RequestDataException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Service
public class DataManagementService {

    private static final Logger logger = LoggerFactory.getLogger(DataManagementService.class);
    private static final String IMAGE_TYPE = "image/jpeg";
    private static final String AUDIO_TYPE = "audio/mp3";
    
    private String storagePath;
    
    
    @Autowired
    public DataManagementService(final Environment env) {
        storagePath = env.getRequiredProperty("storage.path");
        storagePath = storagePath.replaceFirst("file:", "");
    }
    
    
    public void saveAlbumData(final String authorName, final String albumTitle, final MultipartFile[] audioFiles) throws IOException {
        for (byte i = 0; i < audioFiles.length; i++) {
            if (!audioFiles[i].getContentType().equals(AUDIO_TYPE)) {
                throw new RequestDataException("Неверный тип файла " + audioFiles[i].getOriginalFilename() + ". Должен быть mp3.");
            }
        }
        
        final StringBuilder path = new StringBuilder(storagePath);
        path.append(authorName);
        File fileSys = new File(path.toString());
        if (!fileSys.exists()) {
            fileSys.mkdir();
        }
        path.append('/').append(albumTitle);
        fileSys = new File(path.toString());
        if (!fileSys.exists()) {
            fileSys.mkdir();
        }
        
        path.append("/%s%s");
        final String pathPattern = path.toString();

        String fileType;
        for (byte i = 0; i < audioFiles.length; i++) {
            final MultipartFile audioFile =  audioFiles[i];
            fileType = audioFile.getOriginalFilename();
            fileType = fileType.substring(fileType.lastIndexOf('.'));
            final String audioPath = String.format(pathPattern, Byte.toString((byte) (i + 1)), fileType);
            
            try(BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(audioPath)))) {
                buffStream.write(audioFile.getBytes());
                logger.info("Получен файл: исходное имя = {}; тип = {};  размер = {} Кб.", 
                    audioFile.getOriginalFilename(), audioFile.getContentType(), audioFile.getSize() >> 10);
            } catch (FileNotFoundException ex) {
                logger.warn("Что-то пошло не так с сохранением аудио файла", ex);
                throw ex;
            } catch (IOException ex) {
                 logger.warn("Что-то пошло не так с сохранением обложки аудио файла", ex);
                throw ex;
            }
        }
        
    }
    
    public void deleteAlbum(final String authorName, final String albumTitle) throws IOException {
        final StringBuilder path = new StringBuilder(storagePath);
        path.append(authorName).append('/').append(albumTitle);
        final String pathStr = path.toString();
        final File dir = new File(pathStr);
        if(dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException ex) {
                logger.warn("Ошибка при удалении директории " + pathStr, ex);
                throw ex;
            }
        }
    }
    
    public File getTrack(final String author, final String album, final String track) {
        final StringBuilder path = new StringBuilder(storagePath);
        path.append(author).append('/').append(album).append('/').append(track).append(".mp3");
        return new File(path.toString());
    }
    
}
