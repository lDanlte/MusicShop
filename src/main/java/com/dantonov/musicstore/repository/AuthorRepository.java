
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.Author;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface AuthorRepository extends CrudRepository<Author, UUID> {
    
    
    Author findByName(String name);
    
    List<Author> findByNameContainingIgnoreCase(String name);
    
    List<Author> findAll(Pageable pageable);

}
