
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.repository.AlbumRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class AlbumService {
    
    @Autowired
    protected AlbumRepository albumRepository;
    
    
    
    public Album findById(UUID id) {
        return albumRepository.findOne(id);
    }
    
    public List<Album> findByTitle(String title) {
        return albumRepository.findByTitleOrderByAuthor_NameAsc(title);
    }
    
    public List<Album> findNextPage(Pageable pageable) {
        return albumRepository.findAll(pageable);
    }
    
    public Album save(Album album) {
        return albumRepository.save(album);
    }

}
