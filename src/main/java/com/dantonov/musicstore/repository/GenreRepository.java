
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.Genre;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface GenreRepository extends CrudRepository<Genre, Integer>{
    
    Genre findByName(String name);
    
    List<Genre> findAllByOrderByName();

}
