
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.Album;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface AlbumRepository extends CrudRepository<Album, UUID>{
    
    List<Album> findByTitleOrderByAuthor_NameAsc(String title);
    
    List<Album> findByTracks_Music_NameOrderByTitleAsc(String name);
    
    List<Album> findAll(Pageable pageable);
    
}
