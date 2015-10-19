
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Music;
import com.dantonov.musicstore.repository.MusicRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class MusicService {

    @Autowired
    protected MusicRepository musicRepository;
    
    
    
    public Music findById(UUID id) {
        return musicRepository.findOne(id);
    }
    
    public Music findByName(String name) {
        return musicRepository.findByName(name);
    }
    
    public Music save(Music music) {
        return musicRepository.save(music);
    }
}
