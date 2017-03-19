package com.dantonov.musicstore.service;

import com.dantonov.musicstore.entity.User;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@Service
@Deprecated
public class AuthService {
    
    private static final String TOKEN_NAME = "AUTH-TOKEN";
    private static final String LOGIN_NAME = "LOGIN";
    private static final int MAX_COOKIE_AGE = 30 * 24 * 60 * 60;
    
    
    @Autowired
    protected UserService userService;
    
    
    public User getUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        
        String login = null,
               token = null;
        
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(TOKEN_NAME)) {
                token = cookie.getValue();
            } else if (cookie.getName().equals(LOGIN_NAME)) {
                login = cookie.getValue();
            }
        }
        
        if (login == null || token == null) {
            return null;
        }
        User user = userService.findByLogin(login);
        if (user == null) {
            return null;
        }
        StandardPBEStringEncryptor decryptor = new StandardPBEStringEncryptor();
        decryptor.setPassword(user.getPassword());
        String uuidStr = null;
        try {
            uuidStr = decryptor.decrypt(token);
        } catch (Exception e) {
            return null;
        }
        UUID uuid = null;
        
        try {
            uuid = UUID.fromString(uuidStr);
        } catch (Exception ex) {
            return null;
        }
        
        if (uuid.equals(user.getToken())) {
            return user;
        }
        
        return null;
    }
    
    public void login(User user, HttpServletResponse response) {
        
        Cookie loginCookie = new Cookie(LOGIN_NAME, user.getLogin());
        loginCookie.setMaxAge(MAX_COOKIE_AGE);
        
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(user.getPassword());
        UUID uuid = UUID.randomUUID();
        Cookie tokenCookie = new Cookie(TOKEN_NAME, encryptor.encrypt(uuid.toString()));
        tokenCookie.setMaxAge(MAX_COOKIE_AGE);
        
        user.setToken(uuid);
        userService.update(user);
        
        response.addCookie(loginCookie);
        response.addCookie(tokenCookie);
    }
    
    public void logout(HttpServletRequest request, HttpServletResponse response, User user) {
        
        if (user == null) {
            user = getUser(request);
        }
        user.setToken(null);
        userService.update(user);
        
        Cookie loginCookie = null;
        Cookie tokenCookie = null;
        
        for (Cookie cookie : request.getCookies()) {
            switch (cookie.getName()) {
                case TOKEN_NAME: {
                    tokenCookie = cookie;
                    break;
                }
                case LOGIN_NAME: {
                    loginCookie = cookie;
                    break;
                }
            }
        }
        
        if (loginCookie != null) {
            loginCookie.setMaxAge(0);
            response.addCookie(loginCookie);
        }
        
        if (tokenCookie != null) {
            tokenCookie.setMaxAge(0);
            response.addCookie(tokenCookie);
        }
        
    }

}
