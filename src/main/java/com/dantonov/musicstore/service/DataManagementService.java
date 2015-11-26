package com.dantonov.musicstore.service;

import com.dantonov.musicstore.exception.RequestDataException;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Service
@PropertySource("classpath:app.properties")
public class DataManagementService {

    private static final Logger logger = LoggerFactory.getLogger(DataManagementService.class);
    private static final String IMAGE_TYPE = "image/jpeg";
    private static final String AUDIO_TYPE = "audio/mp3";
    
    private String storagePath;
    
    
    @Autowired
    public DataManagementService(Environment env) {
        storagePath = env.getRequiredProperty("storage.path");
        storagePath = storagePath.replaceFirst("file:", "");
    }
    
    public void saveAuthorCover(String authorName, MultipartFile file) throws IOException {
        
        if (!file.getContentType().equals(IMAGE_TYPE)) {
            throw new RequestDataException("Неверный тип обложки. Должен быть .jpg.");
        }
        
        StringBuilder path = new StringBuilder(storagePath);
        path.append(authorName);
        
        File fileSys = new File(path.toString());
        if (!fileSys.exists()) {
            fileSys.mkdir();
        }
        
        path.append("/cover");
        String fileType = file.getOriginalFilename();
        fileType = fileType.substring(fileType.lastIndexOf('.'));
        path.append(fileType);
        
        logger.info("Created Path = {}",  path);
        fileSys = new File(path.toString());
        
        try(BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(fileSys))) {
            buffStream.write(file.getBytes());
        }
        
        logger.info("Получен файл: имя = {}; исходное имя = {}; тип = {};  размер = {} Кб.", file.getName(),
                    file.getOriginalFilename(), file.getContentType(), file.getSize() >> 10);
        
    }
    
    
    public void saveAlbumData(String authorName, String albumTitle,
                              MultipartFile cover, MultipartFile[] audioFiles) {
        
        if (!cover.getContentType().equals(IMAGE_TYPE)) {
            throw new RequestDataException("Неверный тип обложки. Должен быть .jpg.");
        }
        
        for (byte i = 0; i < audioFiles.length; i++) {
            if (!audioFiles[i].getContentType().equals(AUDIO_TYPE)) {
            throw new RequestDataException("Неверный тип файла " + audioFiles[i].getOriginalFilename() + ". Должен быть mp3.");
        }
        }
        
        StringBuilder path = new StringBuilder(storagePath);
        path.append(authorName).append('/').append(albumTitle);
        
        File fileSys = new File(path.toString());
        if (!fileSys.exists()) {
            fileSys.mkdir();
        }
        
        path.append("/%s%s");
        String pathPattern = path.toString();
        
        
        String fileType = cover.getOriginalFilename();
        fileType = fileType.substring(fileType.lastIndexOf('.'));
        String coverPath = String.format(pathPattern, "cover", fileType);
        
        try(BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(coverPath)))) {
            
            buffStream.write(cover.getBytes());
            logger.info("Получен файл: исходное имя = {}; тип = {};  размер = {} Кб.", 
                    cover.getOriginalFilename(), cover.getContentType(), cover.getSize() >> 10);
            
        } catch (FileNotFoundException ex) {
            logger.warn("Что-то пошло не так с сохранением обложки альбома", ex);
        } catch (IOException ex) {
             logger.warn("Что-то пошло не так с сохранением обложки альбома", ex);
        }
        
        for (byte i = 0; i < audioFiles.length; i++) {
            MultipartFile audioFile =  audioFiles[i];
            fileType = audioFile.getOriginalFilename();
            fileType = fileType.substring(fileType.lastIndexOf('.'));
            String audioPath = String.format(pathPattern, Byte.toString((byte) (i + 1)), fileType);
            
            try(BufferedOutputStream buffStream = new BufferedOutputStream(new FileOutputStream(new File(audioPath)))) {
                buffStream.write(audioFile.getBytes());
                logger.info("Получен файл: исходное имя = {}; тип = {};  размер = {} Кб.", 
                    audioFile.getOriginalFilename(), audioFile.getContentType(), audioFile.getSize() >> 10);
            } catch (FileNotFoundException ex) {
                logger.warn("Что-то пошло не так с сохранением аудио файла", ex);
            } catch (IOException ex) {
                 logger.warn("Что-то пошло не так с сохранением обложки аудио файла", ex);
            }
        }
        
    }
    
    public void deleteAlbum(String authorName, String albumTitle) throws IOException {
        
        StringBuilder path = new StringBuilder(storagePath);
        path.append(authorName).append('/').append(albumTitle);
        String pathStr = path.toString();
        File dir = new File(pathStr);
        if(dir.exists()) {
            try {
                FileUtils.deleteDirectory(dir);
            } catch (IOException ex) {
                logger.warn("Ошибка при удалении директории " + pathStr, ex);
                throw ex;
            }
        }
    }
    
    public File getTrack(String author, String album, String track) {
        
        StringBuilder path = new StringBuilder(storagePath);
        path.append(author).append('/').append(album).append('/').append(track).append(".mp3");
        
        return new File(path.toString());
    }
    
}
