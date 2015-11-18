
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.Track;

import java.util.List;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface TrackRepository extends CrudRepository<Track, UUID>{

    List<Track> findByNameContainingIgnoreCase(String name);
    
}
