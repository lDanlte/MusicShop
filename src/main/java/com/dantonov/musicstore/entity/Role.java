
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

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "roles")
public class Role {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "role", nullable = false, updatable = false, length = 16)
    private String role;
    

    @ManyToMany(targetEntity = User.class)
    @JoinTable(name = "user_to_role",
               joinColumns = @JoinColumn(name = "role_id", nullable = false),
               inverseJoinColumns = @JoinColumn(name = "user_id", nullable = false))
    private Set<User> users;

    
    
    public Role() {
    }

    public Role(final String role) {
        this.role = role;
    }
    
    
    public Integer getId() { return id; }
    public void setId(final Integer id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(final String role) { this.role = role; }

    public Set<User> getUsers() { return users; }
    public void setUsers(final Set<User> users) { this.users = users; }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Role role = (Role) o;

        return id.equals(role.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
