
package com.dantonov.musicstore.entity;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "Authors")
public class Author {

    @Id
    @Column(name = "author_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "name", nullable = false, updatable = false, length = 32)
    private String name;
    
    @Column(name = "icon", nullable = false, length = 64)
    private String icon;
    
    @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "author", targetEntity = Album.class)
    @OrderBy("release_date desc")
    private List<Album> albums;

    
    
    public Author() { }

    public Author(String name, String icon, User user) {
        this.name = name;
        this.icon = icon;
        this.user = user;
    }
    
    

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public List<Album> getAlbums() { return albums; }
    public void setAlbums(List<Album> albums) { this.albums = albums; }
    
    
    
}
