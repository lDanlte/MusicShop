
package com.dantonov.musicstore.entity;

import java.util.List;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.GenericGenerator;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */

@Entity
@Table(name = "Music")
public class Music {
    
    @Id
    @Column(name = "music_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    private UUID id;
    
    @Column(name = "name", nullable = false, updatable = false, length = 32)
    private String name;
    
    @Column(name = "description")
    private String desc;
    
    @OneToMany(mappedBy = "music", targetEntity = Track.class)
    @OrderBy("duration desc")
    private List<Track> tracks;

    
    
    public Music() {}

    public Music(String name, String desc) {
        this.name = name;
        this.desc = desc;
    }

    public Music(String name, String desc, List<Track> tracks) {
        this.name = name;
        this.desc = desc;
        this.tracks = tracks;
    }
    
    

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public List<Track> getTracks() { return tracks; }
    public void setTracks(List<Track> tracks) { this.tracks = tracks; }
    
    

}
