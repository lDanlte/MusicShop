package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.UserDto;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.RoleService;
import com.dantonov.musicstore.service.UserService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("UserController")
@RequestMapping(value = "/user")
public class UserController {

    private static final String USER_ROLE = "USER";
    
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private GenreService genreService;
    
    
    
    
    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public ModelAndView dashboard(@PathVariable("userName") String user, ModelAndView modelAndView) {
        
        modelAndView.addObject("isAdmin", true);
        modelAndView.addObject("isAuthor", true);
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        modelAndView.setViewName("dashboard");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/{userName}/boughtAlbums", method = RequestMethod.GET)
    public ModelAndView albums(@PathVariable("userName") String userName, ModelAndView modelAndView) {
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("AuthorName_1", 5);
        map.put("AuthorName_2", 8);
        map.put("AuthorName_3", 2);
        map.put("AuthorName_4", 3);
        map.put("AuthorName_5", 2);
        map.put("AuthorName_6", 19);
        modelAndView.addObject("dataMap", map);
        
        modelAndView.setViewName("index");
        return modelAndView;
    }
    
    
    
    @RequestMapping(value = "/registration", method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public void addUser(@RequestBody UserDto newUser) {
        
        User user = new User(newUser.getLogin(), newUser.getPass(), newUser.getEmail());
        
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByName(USER_ROLE));
        user.setRoles(roles);
        
        userService.saveUser(user);
    }
    
}
