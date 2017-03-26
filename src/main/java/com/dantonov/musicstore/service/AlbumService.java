
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.Track;
import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.AppSQLException;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.repository.AlbumRepository;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class AlbumService {
    
    private static final Logger log = LoggerFactory.getLogger(AlbumService.class);
    private static final Integer BUY_ACTION = 1;
    
    
    @Autowired
    protected AlbumRepository albumRepository;
    
    @Autowired
    protected UserService userService;
    
    @Autowired
    protected ActionService actionService;
    
    @Autowired
    protected TradeHistoryService historyService;

    @Autowired
    private MongoDataStorageService storageService;
    
    
    public Album findById(final UUID id) {
        return albumRepository.findOne(id);
    }
    
    public List<Album> findByTitle(final String title) {
        return albumRepository.findByTitleOrderByAuthor_NameAsc(title);
    }
    
    public Album findByTitleAndAuthor(final String title, final String authorName) {
        return albumRepository.findByTitleAndAuthor_Name(title, authorName);
    }
    
    public List<Album> findNextPage(final Pageable pageable) {
        return albumRepository.findAll(pageable);
    }
    
    public List<Album> getLastAdded() {
        return albumRepository.findTop20ByOrderByAddDateDesc();
    }
    
    public List<Album> getTopSales() {
        return albumRepository.findTop20ByOrderByQSoldDesc();
    }
    
    public List<Album> searchByTitle(final String titlePattern) {
        return albumRepository.findByTitleContainingIgnoreCase(titlePattern);
    }
    
    public List<Album> getLastAddedByGenre(final Genre genre) {
        final List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        return albumRepository.findTop20ByGenresOrderByAddDateDesc(genres);
    }
    
    public List<Album> getTopSalesByGenre(final Genre genre) {
        final List<Genre> genres = new ArrayList<>();
        genres.add(genre);
        return albumRepository.findTop20ByGenresOrderByQSoldDesc(genres);
    }
    
    @Transactional
    public User buy(final Album album, User user, final BigDecimal price) {
        final BigDecimal userWallet = user.getWallet();
        if (userWallet.compareTo(price) < 0) {
            throw new NotEnoughMoneyException("Недостаточно денег на счету.");
        }
        
        try {
            user.setWallet(userWallet.subtract(price));
            user.getAlbums().add(album);
            user = userService.update(user);

            final User uAuthor = album.getAuthor().getUser();
            uAuthor.setWallet(uAuthor.getWallet().add(price));
            userService.update(uAuthor);
            
            final TradeHistory history = new TradeHistory();
            history.setAlbum(album);
            history.setUser(user);
            history.setPrice(price);
            history.setAction(actionService.findById(BUY_ACTION));
            history.setDatetime(new Date());
            historyService.save(history);

            if (price.compareTo(BigDecimal.ZERO) != 0) {
                long qSold = album.getqSold();
                qSold++;
                album.setqSold(qSold);
                albumRepository.save(album);
            }
        } catch (RuntimeException ex) {
            log.warn("Пользователю {} не удалось купить альбом {} от группы {}.",
                     user.getLogin(), album.getTitle(), album.getAuthor().getName(), ex);
            throw new AppSQLException("Не удалось купить альбом. Попробуйте позднее.");
        }
        return user;
    }
    
    @Transactional
    public void create(final Album album,
                       final User user,
                       final MultipartFile cover,
                       final MultipartFile[] tracks,
                       final List<Genre> genres) {

        GridFSInputFile file = null;
        List<String> savedFiles = null;
        try {
            for(final Genre genre: genres) {
                genre.getAlbums().add(album);
            }
            album.setGenres(genres);
            file = storageService.save(
                    cover.getInputStream(),
                    album.getAuthor().getName() + " " + album.getTitle() + " album cover"
                            + cover.getOriginalFilename().substring(cover.getOriginalFilename().lastIndexOf('.')),
                    cover.getContentType(),
                    null
            );
            album.setCoverId(file.getId().toString());
            savedFiles = saveAlbumData(album.getAuthor().getName(), album.getTracks().stream().map(Track::getName).collect(Collectors.toList()), tracks);
            for (int i = 0; i < savedFiles.size(); i++) {
                album.getTracks().get(i).setFileId(savedFiles.get(i));
            }
            save(album);
            buy(album, user, BigDecimal.ZERO);
        } catch (final Exception ex) {
            log.warn("Ошибка при добавлении альбома.", ex);
            if (file != null) {
                storageService.delete(file.getId().toString());
            }
            if (savedFiles != null) {
                savedFiles.forEach(storageService::delete);
            }
            throw new RequestDataException("Ошибка при добавлении альбома.");
        }
        
    }
    
    public Album save(final Album album) {
        album.setqSold(0L);
        album.setAddDate(new Date());
        return albumRepository.save(album);
    }
    
    public Album update(final Album album) {
         return albumRepository.save(album);
     }

    private List<String> saveAlbumData(final String authorName, final List<String> audioNames, final MultipartFile[] audioFiles) throws IOException {
        for (byte i = 0; i < audioFiles.length; i++) {
            if (!"audio/mp3".equals(audioFiles[i].getContentType())) {
                throw new RequestDataException("Неверный тип файла " + audioFiles[i].getOriginalFilename() + ". Должен быть mp3.");
            }
        }
        final List<String> ids = new ArrayList<>();
        for (byte i = 0; i < audioFiles.length; i++) {
            final MultipartFile audioFile = audioFiles[i];
            String fileType = audioFile.getOriginalFilename();
            fileType = fileType.substring(fileType.lastIndexOf('.'));
            final String newName = authorName + " - " + audioNames.get(i) + fileType;
            final GridFSInputFile gridFSInputFile = storageService.save(audioFile.getInputStream(), newName, audioFile.getContentType(), null);
            ids.add(gridFSInputFile.getId().toString());
            log.info("Получен файл: имя = {}; тип = {}; размер = {} Кб.",
                    newName, audioFile.getContentType(), audioFile.getSize() >> 10);
        }
        return ids;
    }

}
