
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.repository.AuthorRepository;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class AuthorService {
    
    @Autowired
    protected AuthorRepository authorRepository;
    
    
    
    public Author findById(UUID id) {
        return authorRepository.findOne(id);
    }
    
    public Author findByName(String name) {
        return authorRepository.findByName(name);
    }
    
    public List<Author> searchByName(String namePattern) {
        return authorRepository.findByNameContainingIgnoreCase(namePattern);
    }
    
    public Author save(Author author) {
        return authorRepository.save(author);
    }
    
    

}
