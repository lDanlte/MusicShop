
package com.dantonov.musicstore.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "Genres")
public class Genre {
    
    @Id
    @Column(name = "genre_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Byte id;
    
    @Column(name = "name", nullable = false, updatable = false, length = 32)
    private String name;
    
    @ManyToMany(targetEntity = Album.class)
    @JoinTable(name = "Albums_to_Genres",
               joinColumns = @JoinColumn(name = "genre_id", nullable = false),
               inverseJoinColumns = @JoinColumn(name = "album_id", nullable = false))
    @OrderBy("title")
    private List<Album> albums;

    
    
    public Genre() {}

    public Genre(String name) {
        this.name = name;
    }

    
    
    public Byte getId() { return id; }
    public void setId(Byte id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Album> getAlbums() { return albums; }
    public void setAlbums(List<Album> albums) { this.albums = albums; }
    
    

}
