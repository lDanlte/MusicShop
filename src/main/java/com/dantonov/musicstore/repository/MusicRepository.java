
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.Music;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface MusicRepository extends CrudRepository<Music, UUID>{

    Music findByName(String name);
    
}
