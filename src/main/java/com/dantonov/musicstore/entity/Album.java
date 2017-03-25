
package com.dantonov.musicstore.entity;

import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "albums")
public class Album {
    
    @Id
    @Column(name = "id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "title", nullable = false, updatable = false, length = 48)
    private String title;
    
    @Column(name = "price", columnDefinition = "DECIMAL(18,2)", nullable = false)
    private BigDecimal price;
    
    @Column(name = "release_date", columnDefinition = "datetime", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date releaseDate;
    
    @Column(name = "add_date", columnDefinition = "datetime", nullable = false, updatable = false)
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date addDate;
    
    @Column(name = "description")
    private String desc;
    
    @Column(name = "q_sold", nullable = false)
    private Long qSold;

    @Column(name = "cover_id", columnDefinition = "CHAR(24)")
    private String coverId;

    @ManyToMany(mappedBy = "albums", targetEntity = User.class)
    private Set<User> users;
    
    @Cascade(CascadeType.ALL)
    @ManyToMany(mappedBy = "albums", targetEntity = Genre.class)
    @OrderBy("name")
    private List<Genre> genres;
    
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Author.class)
    @JoinColumn(name = "author_id", nullable = false, updatable = false)
    private Author author;
    
    @OneToMany(mappedBy = "album", targetEntity = TradeHistory.class)
    @OrderBy("datetime desc")
    private List<TradeHistory> historys;
    
    @Cascade(CascadeType.ALL)
    @OneToMany(mappedBy = "album", targetEntity = Track.class)
    @OrderBy("pos")
    private List<Track> tracks;

    @Transient
    private boolean isBought = false;
    
    
    public Album() {
    }

    public Album(final Author author, final String title, final List<Track> tracks, final BigDecimal price,
                 final Date releaseDate) {
        this.author = author;
        this.title = title;
        this.tracks = tracks;
        this.price = price;
        this.releaseDate = releaseDate;
        this.addDate = new Date();
        qSold = 0L;
    }

    public Album(final Author author, final String title, final List<Track> tracks, final BigDecimal price,
                 final Date releaseDate, final Date addDate, final String desc) {
        this.author = author;
        this.title = title;
        this.tracks = tracks;
        this.price = price;
        this.releaseDate = releaseDate;
        this.addDate = addDate;
        this.desc = desc;
        qSold = 0L;
    }

    
    
    public UUID getId() { return id; }
    public void setId(final UUID id) { this.id = id; }

    public String getTitle() {  return title; }
    public void setTitle(final String title) { this.title = title; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(final BigDecimal price) { this.price = price; }

    public Date getReleaseDate() { return releaseDate; }
    public void setReleaseDate(final Date releaseDate) { this.releaseDate = releaseDate; }

    public Date getAddDate() { return addDate; }
    public void setAddDate(final Date addDate) { this.addDate = addDate; }
    
    public String getDesc() { return desc; }
    public void setDesc(final String desc) { this.desc = desc; }

    public Long getqSold() { return qSold; }
    public void setqSold(final Long qSold) { this.qSold = qSold;  }

    public String getCoverId() { return coverId; }
    public void setCoverId(final String coverId) { this.coverId = coverId; }
    
    public Set<User> getUsers() { return users; }
    public void setUsers(final Set<User> users) { this.users = users; }

    public List<Genre> getGenres() { return genres; }
    public void setGenres(final List<Genre> genres) { this.genres = genres; }

    public Author getAuthor() { return author; }
    public void setAuthor(final Author author) { this.author = author; }

    public List<TradeHistory> getHistorys() { return historys; }
    public void setHistorys(final List<TradeHistory> historys) { this.historys = historys; }

    public List<Track> getTracks() { return tracks; }
    public void setTracks(final List<Track> tracks) { this.tracks = tracks; }

    public boolean getIsBought() { return isBought; }
    public void setIsBought(final boolean isBought) { this.isBought = isBought; }
    
    
    
    @Override
    public boolean equals(final Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Album)) return false;
        
        final Album album = (Album) obj;
        return id.equals(album.getId());

    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.id);
        return hash;
    }
    

}
