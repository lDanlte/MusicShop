
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class RoleService {
    
    @Autowired
    protected RoleRepository roleRepository;
    
    
    
    public Role findById(Byte id) {
        return roleRepository.findOne(id);
    }
    
    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }
    
    public Role findByName(String name) {
        return roleRepository.findByRole(name);
    }
    
    public Role save(Role role) {
        return roleRepository.save(role);
    }

}
