package com.dantonov.musicstore.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@Controller("AlbumController")
@RequestMapping(value = "/album")
public class AlbumController {

    @RequestMapping(value = "/{albumName}", method = RequestMethod.GET)
    public ModelAndView albumPage(@PathVariable("albumName") String albumName, ModelAndView modelAndView) {
        modelAndView.addObject("isBought", true);
        
        List<String> genres = new ArrayList<>();
        genres.add("Рок");
        genres.add("Репчик");
        genres.add("Джаз");
        genres.add("Народное");
        genres.add("Попса");
        modelAndView.addObject("genres", genres);
        
        modelAndView.setViewName("album");
        
        return modelAndView;
    }
    
}
