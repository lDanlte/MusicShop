
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.User;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface UserRepository extends CrudRepository<User, UUID>{
    
    User findByLogin(String Login);
    
    User findByEmail(String email);

}