
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
    
    List<Author> findAll(Pageable pageable);
    
    Author findByName(String name);

}
