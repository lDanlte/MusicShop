
package com.dantonov.musicstore.entity;

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
    
    @Column(name = "duration", nullable = false, updatable = false)
    private Integer duration;
    
    @Column(name = "size", nullable = false, updatable = false)
    private Long size;
    
    @Column(name = "src", nullable = false, length = 64)
    private String src;
    
    @ManyToOne(targetEntity = Album.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "album_id", nullable = false, updatable = false)
    private Album album;
    
    @ManyToOne(targetEntity = Music.class)
    @JoinColumn(name = "music_id", nullable = false, unique = false)
    private Music music;

    
    
    public Track() {
    }

    public Track(Album album, Music music, Integer duration, Long size, String src) {
        this.album = album;
        this.music = music;
        this.duration = duration;
        this.size = size;
        this.src = src;
    }

    
    
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }

    public String getSrc() { return src; }
    public void setSrc(String src) { this.src = src; }

    public Album getAlbum() { return album; }
    public void setAlbum(Album album) { this.album = album; }

    public Music getMusic() { return music; }
    public void setMusic(Music music) { this.music = music; }
    
    
    
}
