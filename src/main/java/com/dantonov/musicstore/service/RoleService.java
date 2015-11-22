
package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Service
public class RoleService {
    
    @Autowired
    protected RoleRepository roleRepository;
    
    
    
    public Role findById(Integer id) {
        return roleRepository.findOne(id);
    }
    
    public Iterable<Role> findAll() {
        return roleRepository.findAll();
    }
    
    public Role findByName(String name) {
        return roleRepository.findByRoleIgnoreCase(name);
    }
    
    public Role save(Role role) {
        return roleRepository.save(role);
    }

}
