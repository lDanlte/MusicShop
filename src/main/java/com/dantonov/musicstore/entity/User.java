
package com.dantonov.musicstore.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
@Entity
@Table(name = "Users")
public class User {
    
    @Id
    @Column(name = "user_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "login", nullable = false, updatable = false, length = 16)
    private String login;
    
    @Column(name = "pass", nullable = false, length = 32)
    private String password;
    
    @Column(name = "email", nullable = false, length = 32)
    private String email;
    
    @Column(name = "wallet", columnDefinition = "money", scale = 2)
    private BigDecimal wallet;
    
    @Column(name = "token", columnDefinition = "BINARY(16)")
    private UUID token;
    
    @Cascade(CascadeType.ALL)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, targetEntity = Author.class)
    private Author author;
    
    @Cascade(CascadeType.ALL)
    @ManyToMany(mappedBy = "users", targetEntity = Role.class)
    private Set<Role> roles;
    
    //@PersistenceContext(type = PersistenceContextType.EXTENDED)
    @ManyToMany(targetEntity = Album.class)
    @JoinTable(name = "Users_to_Albums",
               joinColumns = @JoinColumn(name = "user_id", nullable = false),
               inverseJoinColumns = @JoinColumn(name = "album_id", nullable = false))
    @OrderBy("title")
    private List<Album> albums;
    
    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "user", targetEntity = TradeHistory.class)
    @OrderBy("datetime desc")
    private List<TradeHistory> historys;

    
    
    public User() {}
    
    public User(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public User(String login, String password, String email, Set<Role> roles) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.roles = roles;
    }


     
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) {  this.email = email; }

    public BigDecimal getWallet() { return wallet; }
    public void setWallet(BigDecimal wallet) {  this.wallet = wallet; }

    public UUID getToken() { return token; }
    public void setToken(UUID token) { this.token = token; }
    
    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public List<Album> getAlbums() { return albums; }
    public void setAlbums(List<Album> albums) { this.albums = albums; }

    public void setHistorys(List<TradeHistory> historys) { this.historys = historys; }
    public List<TradeHistory> getHistorys() { return historys; }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof User)) return false;
        
        User user = (User) obj;
        if (! id.equals(user.getId())) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    
}
