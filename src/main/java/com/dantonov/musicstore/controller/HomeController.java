package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.service.AlbumService;
import com.dantonov.musicstore.service.AuthorService;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.TrackService;
import java.text.DecimalFormat;

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
    
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat();
    static {
        DEC_FORMAT.setMaximumFractionDigits(2);
        DEC_FORMAT.setMinimumFractionDigits(2);
        DEC_FORMAT.setGroupingUsed(false);
    }
    
    @Autowired
    private GenreService genreService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private TrackService trackService;
    
    
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView home(ModelAndView modelAndView) {
        
        modelAndView.addObject("pageContextStr", "index");
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        Map<String, List<Album>> map = new LinkedHashMap<>();
        map.put("Последние добавленные", albumService.getLastAdded());
        map.put("Топ продаж", albumService.getTopSales());
        modelAndView.addObject("dataMap", map);
        
        modelAndView.addObject("format", DEC_FORMAT);
        
        modelAndView.setViewName("index");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchingPage(@RequestParam("q") String q, ModelAndView modelAndView) {
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        modelAndView.addObject("authors", authorService.searchByName(q));
        modelAndView.addObject("albums", albumService.searchByTitle(q));
        modelAndView.addObject("tracks", trackService.searchByName(q));
        modelAndView.addObject("format", DEC_FORMAT);
        
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
        
        Map<String, List<Album>> map = new LinkedHashMap<>();
        map.put("Последние добавленные - " + selectedGenre.getName(), albumService.getLastAddedByGenre(selectedGenre));
        map.put("Топ продаж - " + selectedGenre.getName(), albumService.getTopSalesByGenre(selectedGenre));
        modelAndView.addObject("dataMap", map);
        
        modelAndView.addObject("format", DEC_FORMAT);
        
        modelAndView.setViewName("index");
        
        return modelAndView;
    }
}
