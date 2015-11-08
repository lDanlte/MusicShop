package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.service.GenreService;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private GenreService genreService;
    
    @RequestMapping(value = "/{albumName}", method = RequestMethod.GET)
    public ModelAndView albumPage(@PathVariable("albumName") String albumName, ModelAndView modelAndView) {
        modelAndView.addObject("isBought", true);
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        modelAndView.setViewName("album");
        
        return modelAndView;
    }
    
}
