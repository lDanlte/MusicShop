package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public class AlbumDto {
    
    @JsonProperty(value = "title", required = true)
    private String title;
    
    @JsonProperty(value = "desc", required = true)
    private String desc;
    
    @JsonProperty(value = "price", required = true)
    private String price;
    
    @JsonProperty(value = "releaseDate", required = true)
    private String releaseDate;
    
    @JsonProperty(value = "genresIds", required = true)
    private String genresIds;
    
    @JsonProperty(value = "songsTitles", required = true)
    private List<String> songsTitles;

    
    
    public AlbumDto() {
    }

    public AlbumDto(String title, String desc, String price, String releaseDate, String genresIds, List<String> songsTitles) {
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.releaseDate = releaseDate;
        this.genresIds = genresIds;
        this.songsTitles = songsTitles;
    }

    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }

    public String getReleaseDate() { return releaseDate; }
    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getGenresIds() { return genresIds; }
    public void setGenresIds(String genresIds) { this.genresIds = genresIds; }

    public List<String> getSongsTitles() { return songsTitles; }
    public void setSongsTitles(List<String> songsTitles) { this.songsTitles = songsTitles; }
    
    
}
