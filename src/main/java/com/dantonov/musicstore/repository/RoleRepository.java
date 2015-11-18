
package com.dantonov.musicstore.repository;

import com.dantonov.musicstore.entity.Role;

import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public interface RoleRepository extends CrudRepository<Role, Integer>{

    Role findByRoleIgnoreCase(String role);
    
}
