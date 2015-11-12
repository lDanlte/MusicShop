
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.repository.TradeHistoryRepository;
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
public class TradeHistoryService {

    private static final long DAY_DURATION = 24 * 60 * 60 * 1000;
    
    @Autowired
    protected TradeHistoryRepository historyRepository;
    
    
    
    public TradeHistory findById(UUID id) {
        return historyRepository.findOne(id);
    }
    
    public List<TradeHistory> findBetweenDays(User user, Date from, Date to) {
        
        to.setTime(to.getTime() + DAY_DURATION);
        
        return historyRepository.findBetweenDates(user.getId(), from, to);
    }
    
    public List<TradeHistory> findNextPage(Pageable pageable) {
        return historyRepository.findAll(pageable);
    }
    
    @Transactional
    public TradeHistory save(TradeHistory tradeHistory) {
        return historyRepository.save(tradeHistory);
    }
}
