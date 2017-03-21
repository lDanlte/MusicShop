package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.PageNotFoundException;
import com.dantonov.musicstore.service.AlbumService;
import com.dantonov.musicstore.service.AuthorService;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.TrackService;
import com.dantonov.musicstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("HomeController")
public class HomeController {

    
    @Autowired
    private GenreService genreService;
    
    @Autowired
    private AlbumService albumService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private TrackService trackService;

    @Autowired
    private UserService userService;

    @Autowired
    private DecimalFormat decimalFormat;



    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView home(final ModelAndView modelAndView,
                             final RedirectAttributes redirectAttributes,
                             final Authentication authentication) {

        final User user = (authentication == null)
                ? null
                : userService.findByLogin(authentication.getName());
        
        modelAndView.addObject("pageContextStr", "index");
       
        final Map<String, List<Album>> map = new LinkedHashMap<>();
        final List<Album> lastAdded = albumService.getLastAdded(true);
        final List<Album> topSales = albumService.getTopSales(true);
        if (user != null) {
            setIsBought(lastAdded, user);
            setIsBought(topSales, user);
        }
        
        map.put("Последние добавленные", lastAdded);
        map.put("Топ продаж", topSales);
        modelAndView.addObject("dataMap", map);

        modelAndView.addObject("genres", genreService.findAll(true));
        modelAndView.addObject("user", user);
        modelAndView.addObject("format", decimalFormat);
        
        final Map<String, ?> atr = redirectAttributes.getFlashAttributes();
        if (atr.containsKey("redirectCause")) {
            modelAndView.addObject("redirectCause", atr.get("redirectCause"));
        }
        modelAndView.setViewName("index");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView searchingPage(@RequestParam("q") final String q,
                                      final ModelAndView modelAndView,
                                      final Authentication authentication) {

        final User user = (authentication == null)
                ? null
                : userService.findByLogin(authentication.getName());
        
        modelAndView.addObject("authors", authorService.searchByName(q));
        final List<Album> albums = albumService.searchByTitle(q);
        if (user != null) {
            setIsBought(albums, user);
            System.out.println(authentication.getAuthorities());
        }
        modelAndView.addObject("albums", albums);
        modelAndView.addObject("tracks", trackService.searchByName(q));
        
        modelAndView.addObject("format", decimalFormat);
        modelAndView.addObject("genres", genreService.findAll(true));
        modelAndView.addObject("user", user);
        
        modelAndView.setViewName("searchingResults");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/category", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView categoryPage(@RequestParam("gid") final Integer gid,
                                     final ModelAndView modelAndView,
                                     final Authentication authentication) {

        final User user = (authentication == null)
                ? null
                : userService.findByLogin(authentication.getName());
        
        final Genre selectedGenre = genreService.findById(gid);
        if (selectedGenre == null) {
            throw new PageNotFoundException("Жанра с id" + gid + "не существует.");
        }
        
        modelAndView.addObject("selectedGenreId", selectedGenre.getId());
        modelAndView.addObject("pageContextStr", "index");
        
        final List<Album> lastAdded = albumService.getLastAddedByGenre(selectedGenre);
        final List<Album> topSales = albumService.getTopSalesByGenre(selectedGenre);
        if (user != null) {
            setIsBought(lastAdded, user);
            setIsBought(topSales, user);
        }
        
        final Map<String, List<Album>> map = new LinkedHashMap<>();
        map.put("Последние добавленные - " + selectedGenre.getName(), lastAdded);
        map.put("Топ продаж - " + selectedGenre.getName(), topSales);
        modelAndView.addObject("dataMap", map);
        
        modelAndView.addObject("format", decimalFormat);
        
        modelAndView.setViewName("index");
        modelAndView.addObject("genres", genreService.findAll(true));
        modelAndView.addObject("user", user);
        
        return modelAndView;
    }
    
    private void setIsBought(final List<Album> albums, final User user) {
        for (final Album album : albums) {
            if (user.hasAlbum(album)) {
                album.setIsBought(true);
            }
        }
    }
    
    
}
