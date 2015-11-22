
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.repository.GenreRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class GenreService {
    
    @Autowired
    protected GenreRepository genreRepository;
    
    
    
    public Genre findById(Integer id) {
        return genreRepository.findOne(id);
    }
    
    public Genre findByName(String name) {
        return genreRepository.findByName(name);
    }
    
    public List<Genre> findAll() {
        return genreRepository.findAllByOrderByName();
    }
    
    
    public Genre save(Genre genre) {
        return genreRepository.save(genre);
    }
    
    

}
