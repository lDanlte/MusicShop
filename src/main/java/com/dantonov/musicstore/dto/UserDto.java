package com.dantonov.musicstore.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public class UserDto {

    
    @JsonProperty("login")
    private String login;
    
    @JsonProperty("pass")
    private String pass;
    
    @JsonProperty("email")
    private String email;

    
    
    public UserDto() {
    }

    public UserDto(String login, String pass, String email) {
        this.login = login;
        this.pass = pass;
        this.email = email;
    }

    
    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPass() {  return pass; }
    public void setPass(String pass) { this.pass = pass; }
    
    
}
