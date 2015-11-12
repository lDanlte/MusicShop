package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.TradeHistoryDto;
import com.dantonov.musicstore.dto.UserDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.RoleService;
import com.dantonov.musicstore.service.TradeHistoryService;
import com.dantonov.musicstore.service.UserService;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("UserController")
@RequestMapping(value = "/user")
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final String USER_ROLE = "User";
    private static final String AUTHOR_ROLE = "Author";
    private static final String ADMIN_ROLE = "Admin";
    private static final SimpleDateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private GenreService genreService;
    
    @Autowired
    private TradeHistoryService historyService;
    
    
    @RequestMapping(value = "/{login}", method = RequestMethod.GET)
    public ModelAndView dashboard(@PathVariable("login") String login, ModelAndView modelAndView) {
        
        User user = userService.findByLogin(login);
        if (user == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        boolean isAdmin  = false,
                isAuthor = false;
        for (Role role : user.getRoles()) {
            switch (role.getRole()) {
                case AUTHOR_ROLE: { isAuthor = true; break; }
                case ADMIN_ROLE: { isAdmin = true; break; }
            }
        }
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("isAuthor", isAuthor);
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        modelAndView.setViewName("dashboard");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/{login}/boughtAlbums", method = RequestMethod.GET)
    public ModelAndView albums(@PathVariable("login") String login, ModelAndView modelAndView) {
        
        User user = userService.findByLogin(login);
        if (user == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        Map<String, List<Album>> map = new LinkedHashMap<>();
        
        for (Album album : user.getAlbums()) {
            Author author = album.getAuthor();
            if (map.containsKey(author.getName())) {
                map.get(author.getName()).add(album);
            } else {
                List<Album> albums = new ArrayList<>();
                albums.add(album);
                map.put(author.getName(), albums);
            }
        }
        
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
        
        userService.save(user);
    }
    
    @RequestMapping(value = "/{login}/changeData", method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    public void changeUserData(@RequestBody UserDto data, @PathVariable("login") String login ) {
        
        if (data.getEmail()!= null) {
            if (userService.findByEmail(data.getEmail()) != null) {
                //пользователь с таким email уже существует
                return;
            } else {
                User user = userService.findByLogin(login);
                user.setEmail(data.getEmail());
                userService.update(user);
            }
        }
        
        if (data.getPass() != null) {
            User user = userService.findByLogin(login);
            user.setPassword(data.getPass());
            userService.update(user);
        }
    }
    
    
    @RequestMapping(value = "/{login}/addMoney", method = RequestMethod.PUT)
    public void addMoney(@RequestParam("value") String value, @PathVariable("login") String login) {
        
        User user = userService.findByLogin(login);
        userService.addCash(user, new BigDecimal(value));
        
    }
    @RequestMapping(value = "/{login}/discountMoney", method = RequestMethod.PUT)
    public void discountMoney(@RequestParam("value") String value, @PathVariable("login") String login) {
        
        try {
            User user = userService.findByLogin(login);
            userService.discountCash(user, new BigDecimal(value));
        } catch (NotEnoughMoneyException ex) {
            log.warn("У пользователя недостаточно денег для вывода.");
        }
        
    }
    
    @RequestMapping(value = "/{login}/tradehistory", method = RequestMethod.GET, 
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TradeHistoryDto> getTradeHistory(@PathVariable("login") String  login,
                                             @RequestParam("from") String from,
                                             @RequestParam("to") String to) {
        
        try {
            Date fromDate = REQUEST_DATE_FORMAT.parse(from),
                   toDate = REQUEST_DATE_FORMAT.parse(to);
            User user = userService.findByLogin(login);
            if (user == null) {
                return null;
            }
            List<TradeHistory> tradeHistories = historyService.findBetweenDays(user, fromDate, toDate);
            if (tradeHistories == null || tradeHistories.isEmpty()) {
                return null;
            }
            
            return getTHDto(tradeHistories);
        } catch (ParseException ex) {
            log.warn("Задан неправельный формат даты.");
        }
        
        return null;
    }
         
    
    
    
    
    private List<TradeHistoryDto> getTHDto(List<TradeHistory> ths) {
        
        List<TradeHistoryDto> result = new ArrayList<>();
        
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setGroupingUsed(false);
        
        for (TradeHistory th : ths) {
            TradeHistoryDto thDto = new TradeHistoryDto();
            
            thDto.setDatetime(th.getDatetime());
            thDto.setPrice(df.format(th.getPrice()));
            thDto.setAction(th.getAction().getDesc());
            
            Album album = th.getAlbum();
            if (album != null) {
                thDto.setAlbum(album.getTitle());
            }
            
            result.add(thDto);
        }
        
        return result;
    }
    
}
