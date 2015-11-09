
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.TradeHistory;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface TradeHistoryRepository extends CrudRepository<TradeHistory, UUID>{

    @Query(
        value = "SELECT * FROM Trade_History WHERE user_id = ?1 AND datetime BETWEEN ?2 AND ?3 ORDER BY datetime DESC;",
        nativeQuery = true
    )
    List<TradeHistory> findBetweenDates(UUID userId, Date from, Date to);
    
    List<TradeHistory> findAll(Pageable pageable);
}
