
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.repository.GenreRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
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
    
    private List<Genre> allGenresCache;
    
    public Genre findById(final Integer id) {
        return genreRepository.findOne(id);
    }
    
    public Genre findByName(final String name) {
        return genreRepository.findByName(name);
    }
    
    public List<Genre> findAll(final boolean useCache) {
        if (useCache) {
            return new ArrayList<>(allGenresCache);
        } else {
            return genreRepository.findAllByOrderByName();
        }
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 2)
    protected void updateCache() {
        allGenresCache = findAll(false);
    }
    
    public Genre save(final Genre genre) {
        return genreRepository.save(genre);
    }
    

}
