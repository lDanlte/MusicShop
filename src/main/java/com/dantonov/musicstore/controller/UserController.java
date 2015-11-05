package com.dantonov.musicstore.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

    @RequestMapping(value = "/{userName}", method = RequestMethod.GET)
    public ModelAndView dashboard(@PathVariable("userName") String user, ModelAndView modelAndView) {
        
        modelAndView.addObject("isAdmin", true);
        modelAndView.addObject("isAuthor", true);
        
        List<String> genres = new ArrayList<>();
        genres.add("Рок");
        genres.add("Репчик");
        genres.add("Джаз");
        genres.add("Народное");
        genres.add("Попса");
        modelAndView.addObject("genres", genres);
        
        modelAndView.setViewName("dashboard");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/{userName}/boughtAlbums", method = RequestMethod.GET)
    public ModelAndView albums(@PathVariable("userName") String userName, ModelAndView modelAndView) {
        
        List<String> genres = new ArrayList<>();
        genres.add("Рок");
        genres.add("Репчик");
        genres.add("Джаз");
        genres.add("Народное");
        genres.add("Попса");
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
    
}
