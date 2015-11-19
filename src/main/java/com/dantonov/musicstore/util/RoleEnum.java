package com.dantonov.musicstore.util;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public enum RoleEnum {
    USER("USER", "Пользователь"), AUTHOR("AUTHOR", "Автор"), ADMIN("ADMIN", "Администратор");
    
    private final String role;
    private final String localRole;
    
    RoleEnum(String role, String localRole) {
        this.role = role;
        this.localRole = localRole;
    }

    public String getRole() {
        return role;
    }
    
     public String getLocalRole() {
        return localRole;
    }
    
}
