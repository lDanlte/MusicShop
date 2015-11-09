
package com.dantonov.musicstore.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "Trade_History")
public class TradeHistory {

    @Id
    @Column(name = "history_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "price", columnDefinition = "money", nullable = false, updatable = false)
    private BigDecimal price;
    
    @Column(name = "datetime", columnDefinition = "datetime")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date datetime;
    
    @ManyToOne(targetEntity = Action.class)
    @JoinColumn(name = "action_id")
    private Action action;
    
    @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @ManyToOne(targetEntity = Album.class)
    @JoinColumn(name = "album_id")
    private Album album;

    
    
    public TradeHistory() {}

    public TradeHistory(BigDecimal price, Date datetime, Action action, User user) {
        this.price = price;
        this.datetime = datetime;
        this.action = action;
        this.user = user;
    }

    public TradeHistory(BigDecimal price, Date datetime, Action action, User user, Album album) {
        this.price = price;
        this.datetime = datetime;
        this.action = action;
        this.user = user;
        this.album = album;
    }
    
    
    
    
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Date getDatetime() { return datetime; }
    public void setDatetime(Date datetime) { this.datetime = datetime; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Action getAction() { return action; }
    public void setAction(Action action) { this.action = action; }

    public User getUser() { return user;}
    public void setUser(User user) { this.user = user; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
    
}
