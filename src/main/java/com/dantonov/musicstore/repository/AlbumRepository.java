
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Genre;
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
    
    List<Album> findByTitleOrderByAddDateDesc(String title);
    
    Album findByTitleAndAuthor_Name(String title, String name);
    
    List<Album> findByTitleContainingIgnoreCase(String title);
    
    List<Album> findTop20ByOrderByAddDateDesc();
    
    List<Album> findTop20ByOrderByQSoldDesc();
    
    List<Album> findTop20ByGenresOrderByAddDateDesc(List<Genre> genres);
    
    List<Album> findTop20ByGenresOrderByQSoldDesc(List<Genre> genres);
    
    List<Album> findAll(Pageable pageable);
    
}
