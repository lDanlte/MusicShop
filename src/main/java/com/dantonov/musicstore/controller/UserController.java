package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.annotation.Secured;
import com.dantonov.musicstore.dto.ResponseMessageDto;
import com.dantonov.musicstore.dto.TradeHistoryDto;
import com.dantonov.musicstore.dto.UserDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.EmailAlreadyExistsException;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.exception.ResourceNotFoundException;
import com.dantonov.musicstore.inspector.AuthInspector;
import com.dantonov.musicstore.service.RoleService;
import com.dantonov.musicstore.service.TradeHistoryService;
import com.dantonov.musicstore.service.UserService;
import com.dantonov.musicstore.util.RoleEnum;

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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("UserController")
@RequestMapping(value = "/user")
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final SimpleDateFormat REQUEST_DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy");
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat();
    static {
        DEC_FORMAT.setMaximumFractionDigits(2);
        DEC_FORMAT.setMinimumFractionDigits(2);
        DEC_FORMAT.setGroupingUsed(false);
    }
    
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private TradeHistoryService historyService;
    
    
    
    @Secured
    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView dashboard(ModelAndView modelAndView,
                                  HttpServletRequest request) {
        
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
       
        boolean isAdmin  = user.hasRole(RoleEnum.ADMIN.getRole()),
                isAuthor = user.hasRole(RoleEnum.AUTHOR.getRole());
        
        modelAndView.addObject("isAdmin", isAdmin);
        modelAndView.addObject("isAuthor", isAuthor);
        modelAndView.addObject("format", DEC_FORMAT);

        modelAndView.setViewName("dashboard");
        
        return modelAndView;
    }
    
    @Secured
    @RequestMapping(value = "/boughtAlbums", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView albums(ModelAndView modelAndView,
                                HttpServletRequest request) {
        
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
        
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
        modelAndView.addObject("format", DEC_FORMAT);
        
        modelAndView.setViewName("index");
        return modelAndView;
    }
    
    @RequestMapping(value = "/", method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void addUser(@RequestBody UserDto newUser) {
        
        User user = new User(newUser.getLogin(), newUser.getPass(), newUser.getEmail());
        
        Set<Role> roles = new HashSet<>();
        roles.add(roleService.findByName(RoleEnum.USER.getRole()));
        user.setRoles(roles);
        
        userService.save(user);
    }
    
    @Secured
    @RequestMapping(value = "/changeData", method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void changeUserData(@RequestBody UserDto data,
                               HttpServletRequest request) {
        
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
        
        if (data.getEmail()!= null) {
            if (userService.findByEmail(data.getEmail()) != null) {
                throw new EmailAlreadyExistsException("Пользователь с email " + user.getEmail() + " уже существует.");
            } else {
                user.setEmail(data.getEmail());
                userService.update(user);
            }
        }
        
        if (data.getPass() != null) {
            user.setPassword(data.getPass());
            userService.update(user);
        }
    }
    
    @Secured
    @RequestMapping(value = "/addMoney", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void addMoney(@RequestParam("value") String value,
                         HttpServletRequest request) {
        
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
        
        userService.addCash(user, new BigDecimal(value));
    }
    
    @Secured(role = RoleEnum.AUTHOR)
    @RequestMapping(value = "/discountMoney", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    public void discountMoney(@RequestParam("value") String value,
                              HttpServletRequest request) {
        
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);

        userService.discountCash(user, new BigDecimal(value));
        
    }
    
    @Secured
    @RequestMapping(value = "/tradehistory", method = RequestMethod.GET, 
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TradeHistoryDto> getTradeHistory(@RequestParam("from") String from,
                                                 @RequestParam("to") String to,
                                                 HttpServletRequest request) {
       
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
       
        try {
            Date fromDate = REQUEST_DATE_FORMAT.parse(from),
                   toDate = REQUEST_DATE_FORMAT.parse(to);

            List<TradeHistory> tradeHistories = historyService.findBetweenDays(user, fromDate, toDate);
            if (tradeHistories == null || tradeHistories.isEmpty()) {
                throw new ResourceNotFoundException("Ничего не найдено.");
            }
            
            return getTHDto(tradeHistories);
        } catch (ParseException ex) {
            log.warn("Задан неправельный формат даты.");
            throw new RequestDataException("Задан неправельный формат даты.");
        }
        
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
