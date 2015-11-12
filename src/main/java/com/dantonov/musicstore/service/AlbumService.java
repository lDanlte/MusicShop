
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.repository.AlbumRepository;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class AlbumService {
    
    private static final Byte BUY_ACTION = 1;
    
    @Autowired
    protected AlbumRepository albumRepository;
    
    @Autowired
    protected UserService userService;
    
    @Autowired
    protected ActionService actionService;
    
    @Autowired
    protected TradeHistoryService historyService;
    
    public Album findById(UUID id) {
        return albumRepository.findOne(id);
    }
    
    public List<Album> findByTitle(String title) {
        return albumRepository.findByTitleOrderByAuthor_NameAsc(title);
    }
    
    public Album findByTitleAndAuthor(String title, String authorName) {
        return albumRepository.findByTitleAndAuthor_Name(title, title);
    }
    
    public List<Album> findNextPage(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }
    
    @Transactional
    public void buy(Album album, User user, BigDecimal price) throws NotEnoughMoneyException {
        BigDecimal userWallet = user.getWallet();
        if (userWallet.compareTo(price) < 0) {
            throw new NotEnoughMoneyException();
        }
        user.setWallet(userWallet.subtract(price));
        user.getAlbums().add(album);
        userService.save(user);
        
        TradeHistory history = new TradeHistory();
        history.setAlbum(album);
        history.setUser(user);
        history.setPrice(price);
        history.setAction(actionService.findById(BUY_ACTION));
        history.setDatetime(new Date());
        historyService.save(history);
        
        long qSold = album.getqSold();
        qSold++;
        album.setqSold(qSold);
        albumRepository.save(album);
    }
    
    @Transactional
    public Album save(Album album) {
        album.setqSold(0L);
        album.setAddDate(new Date());
        return albumRepository.save(album);
    }

}
