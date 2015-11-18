package com.dantonov.musicstore.util;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public enum RoleEnum {
    USER("USER"), AUTHOR("AUTHOR"), ADMIN("ADMIN");
    
    RoleEnum(String role) {
        this.role = role;
    }
    
    private final String role;

    public String getRole() {
        return role;
    }
    
}
