
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
@Table(name = "genres")
public class Genre {
    
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    
    @Column(name = "name", nullable = false, updatable = false, length = 32)
    private String name;
    
    @ManyToMany(targetEntity = Album.class)
    @JoinTable(name = "album_to_genre",
               joinColumns = @JoinColumn(name = "genre_id", nullable = false),
               inverseJoinColumns = @JoinColumn(name = "album_id", nullable = false))
    @OrderBy("title")
    private List<Album> albums;

    
    
    public Genre() {}

    public Genre(final String name) {
        this.name = name;
    }

    
    public Integer getId() { return id; }
    public void setId(final Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(final String name) { this.name = name; }

    public List<Album> getAlbums() { return albums; }
    public void setAlbums(final List<Album> albums) { this.albums = albums; }


    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final Genre genre = (Genre) o;

        return id.equals(genre.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
