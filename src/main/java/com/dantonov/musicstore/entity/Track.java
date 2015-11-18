
package com.dantonov.musicstore.entity;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "Tracks")
public class Track {
    
    @Id
    @Column(name = "track_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "name", nullable = false, length = 64)
    private String name;
    
    @Column(name = "duration", nullable = false, updatable = false)
    private Integer duration;
    
    @Column(name = "size", nullable = false, updatable = false)
    private Long size;
    
    @Column(name = "bitrate", nullable = false, updatable = false)
    private Integer bitrate;
    
    @Column(name = "pos", nullable = false, updatable = false)
    private Byte position;
    
    @ManyToOne(targetEntity = Album.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false, updatable = false)
    private Album album;

    
    
    public Track() {
    }

    public Track(Album album, String name, Integer duration, Long size, Integer bitrate, Byte position) {
        this.name = name;
        this.duration = duration;
        this.size = size;
        this.bitrate = bitrate;
        this.position = position;
        this.album = album;
    }

    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public Integer getBitrate() { return bitrate; }
    public void setBitrate(Integer bitrate) { this.bitrate = bitrate; }

    public Byte getPosition() { return position; }
    public void setPosition(Byte position) { this.position = position; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }
    
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (obj == this) return true;
        if (!(obj instanceof Track)) return false;
        
        Track track = (Track) obj;
        if (! id.equals(track.getId())) return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    
}
