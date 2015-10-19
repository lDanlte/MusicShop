
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.repository.UserRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class UserService {
    
    @Autowired
    protected UserRepository userRepository;
    
    
    
    public User findById(UUID id) {
        return userRepository.findOne(id);
    }
    
    public User findByLogin(String login) {
        return userRepository.findByLogin(login);
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User save(User user) {
        return userRepository.save(user);
    }

}
