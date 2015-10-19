
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Track;
import com.dantonov.musicstore.repository.TrackRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class TrackService {
    
    @Autowired
    protected TrackRepository trackRepository;
    
    
    
    public Track findById(UUID id) {
        return trackRepository.findOne(id);
    }
    
    public Track save(Track track) {
        return trackRepository.save(track);
    }

}
