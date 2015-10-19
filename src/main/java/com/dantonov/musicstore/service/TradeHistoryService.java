
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.repository.TradeHistoryRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class TradeHistoryService {

    @Autowired
    protected TradeHistoryRepository historyRepository;
    
    
    
    public TradeHistory findById(UUID id) {
        return historyRepository.findOne(id);
    }
    
    public List<TradeHistory> findNextPage(Pageable pageable) {
        return historyRepository.findAll(pageable);
    }
    
    public TradeHistory save(TradeHistory tradeHistory) {
        return historyRepository.save(tradeHistory);
    }
}
