
package com.dantonov.musicstore.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "Albums")
public class Album {
    
    @Id
    @Column(name = "album_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "title", nullable = false, updatable = false, length = 32)
    private String title;
    
    @Column(name = "price", columnDefinition = "money", nullable = false)
    private BigDecimal price;
    
    @Column(name = "release_date", columnDefinition = "datetime", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date releaseDate;
    
    @Column(name = "icon", nullable = false, length = 64)
    private String icon;
    
    @Column(name = "description")
    private String desc;

    @ManyToMany(mappedBy = "albums", targetEntity = User.class)
    private Set<User> users;
    
    @ManyToMany(mappedBy = "albums", targetEntity = Genre.class)
    @OrderBy("name")
    private List<Genre> genres;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Author.class)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private Author author;
    
    @OneToMany(mappedBy = "album", targetEntity = TradeHistory.class)
    @OrderBy("datetime desc")
    private List<TradeHistory> historys;
    
    @OneToMany(mappedBy = "album", targetEntity = Track.class)
    private Set<Track> tracks;

    
    
    public Album() {
    }

    public Album(Author author, String title, Set<Track> tracks, BigDecimal price,
                 Date releaseDate, String icon) {
        this.author = author;
        this.title = title;
        this.tracks = tracks;
        this.price = price;
        this.releaseDate = releaseDate;
        this.icon = icon;
    }

    public Album(Author author, String title, Set<Track> tracks, BigDecimal price,
                 Date releaseDate, String icon, String desc) {
        this.author = author;
        this.title = title;
        this.tracks = tracks;
        this.price = price;
        this.releaseDate = releaseDate;
        this.icon = icon;
        this.desc = desc;
    }

    
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getTitle() {  return title; }
    public void setTitle(String title) { this.title = title; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(Date releaseDate) { this.releaseDate = releaseDate; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public Set<User> getUsers() { return users; }
    public void setUsers(Set<User> users) { this.users = users; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(List<Genre> genres) { this.genres = genres; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public List<TradeHistory> getHistorys() { return historys; }
    public void setHistorys(List<TradeHistory> historys) { this.historys = historys; }

    public Set<Track> getTracks() { return tracks; }
    public void setTracks(Set<Track> tracks) { this.tracks = tracks; }
    
    

}
