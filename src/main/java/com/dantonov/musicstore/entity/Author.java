
package com.dantonov.musicstore.entity;

import java.util.List;
import java.util.Objects;
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
@Table(name = "authors")
public class Author {

    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "name", nullable = false, updatable = false, length = 48)
    private String name;
    
    @Column(name = "description")
    private String desc;

    @Column(name = "cover_id", columnDefinition = "CHAR(24)")
    private String coverId;
    
    @OneToOne(fetch = FetchType.LAZY, targetEntity = User.class)
    @JoinColumn(name = "user_id")
    private User user;
    
    @OneToMany(mappedBy = "author", targetEntity = Album.class)
    @OrderBy("add_date desc")
    private List<Album> albums;

    
    
    public Author() { }

    public Author(final String name, final String desc, final User user) {
        this.name = name;
        this.desc = desc;
        this.user = user;
    }
    

    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    public String getDesc() { return desc; }
    public void setDesc(final String desc) { this.desc = desc; }

    public String getCoverId() { return coverId; }
    public void setCoverId(final String coverId) { this.coverId = coverId; }

    public User getUser() { return user; }
    public void setUser(final User user) { this.user = user; }

    public List<Album> getAlbums() { return albums; }
    public void setAlbums(final List<Album> albums) { this.albums = albums; }
    
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Author)) return false;
        
        final Author author = (Author) obj;
        return id.equals(author.getId());

    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    
}
