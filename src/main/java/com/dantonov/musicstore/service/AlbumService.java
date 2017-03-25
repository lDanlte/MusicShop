
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Genre;
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
    private DataManagementService dataService;

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

        boolean afterSavingData = false;
        GridFSInputFile file = null;
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
            save(album);
            dataService.saveAlbumData(album.getAuthor().getName(), album.getTitle(), tracks);
            afterSavingData = true;
            buy(album, user, BigDecimal.ZERO);
        } catch (final Exception ex) {
            log.warn("Ошибка при добавлении альбома.", ex);
            if (file != null) {
                storageService.delete(file.getId().toString());
            }
            if (afterSavingData) {
                try {
                    dataService.deleteAlbum(album.getAuthor().getName(), album.getTitle());
                } catch (IOException ioex) { }
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

}
