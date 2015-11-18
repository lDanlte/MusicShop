package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public class TradeHistoryDto {

    @JsonProperty("datetime")
    private Date datetime;
    
    @JsonProperty("action")
    private String action;
    
    @JsonProperty("price")
    private String price;
    
    @JsonProperty("album")
    private String album;

    
    
    public TradeHistoryDto() {
    }

    public TradeHistoryDto(Date datetime, String action, String price, String album) {
        this.datetime = datetime;
        this.action = action;
        this.price = price;
        this.album = album;
    }

    
    public Date getDatetime() { return datetime; }
    public void setDatetime(Date datetime) { this.datetime = datetime; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getAlbum() { return album; }
    public void setAlbum(String album) { this.album = album; }

    public String getPrice() {  return price; }
    public void setPrice(String price) { this.price = price; }
    
    
}
