package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.service.GenreService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("HomeController")
public class HomeController {
    
    
    
    @Autowired
    private GenreService genreService;
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(ModelAndView modelAndView) {
        
        modelAndView.addObject("pageContextStr", "index");
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Последние добавленные", 5);
        map.put("Топ продаж", 8);
        modelAndView.addObject("dataMap", map);
        
        modelAndView.setViewName("index");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchingPage(@RequestParam("q") String q, ModelAndView modelAndView) {
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        modelAndView.addObject("authors", 5);
        modelAndView.addObject("albums", 3);
        modelAndView.addObject("tracks", 5);
        
        modelAndView.setViewName("searchingResults");
        return modelAndView;
    }
    
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    public ModelAndView categoryPage(@RequestParam("gid") Integer gid, ModelAndView modelAndView) {
        
        Genre selectedGenre = genreService.findById(gid);
        if (selectedGenre == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        
        modelAndView.addObject("selectedGenreId", selectedGenre.getId());
        modelAndView.addObject("pageContextStr", "index");
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        Map<String, Integer> map = new LinkedHashMap<>();
        map.put("Последние добавленные - " + selectedGenre.getName(), 5);
        map.put("Топ продаж - " + selectedGenre.getName(), 8);
        modelAndView.addObject("dataMap", map);
        
        modelAndView.setViewName("index");
        
        return modelAndView;
    }
}
