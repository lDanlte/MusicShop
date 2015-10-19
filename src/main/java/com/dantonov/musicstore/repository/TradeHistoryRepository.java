
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.TradeHistory;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface TradeHistoryRepository extends CrudRepository<TradeHistory, UUID>{

    List<TradeHistory> findAll(Pageable pageable);
}
