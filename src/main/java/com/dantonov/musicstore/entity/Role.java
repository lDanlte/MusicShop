
package com.dantonov.musicstore.entity;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "Roles")
public class Role {
    
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "role", nullable = false, updatable = false, length = 16)
    private String role;
    

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "Users_to_Roles",
               joinColumns = @JoinColumn(name = "role_id", nullable = false),
               inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false))
    private Set<User> users;

    
    
    public Role() {
    }

    public Role(String role) {
        this.role = role;
    }

    
    
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }
    
}
