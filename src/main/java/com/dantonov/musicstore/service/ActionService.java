
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Action;
import com.dantonov.musicstore.repository.ActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
@Service
public class ActionService {
    
    @Autowired
    protected ActionRepository actionRepository;
    
    
    
    public Action findById(Byte id) {
        return actionRepository.findOne(id);
    }
    
    public Action save(Action action) {
        return actionRepository.save(action);
    }
}
