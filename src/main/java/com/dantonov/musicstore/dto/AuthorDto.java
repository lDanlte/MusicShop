package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public class AuthorDto {

    @JsonProperty("name")
    private String name;
    
    @JsonProperty("desc")
    private String desc;
    
    @JsonProperty("user")
    private UserDto user;

    public AuthorDto() {
    }

    public AuthorDto(String name, String desc, UserDto user) {
        this.name = name;
        this.desc = desc;
        this.user = user;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public UserDto getUser() { return user; }
    public void setUser(UserDto user) { this.user = user; }
    
    
    
}
