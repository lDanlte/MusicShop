package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.ResponseMessageDto;
import com.dantonov.musicstore.dto.TradeHistoryDto;
import com.dantonov.musicstore.dto.UserDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.entity.TradeHistory;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.EmailAlreadyExistsException;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.exception.ResourceNotFoundException;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.RoleService;
import com.dantonov.musicstore.service.TradeHistoryService;
import com.dantonov.musicstore.service.UserService;
import com.dantonov.musicstore.util.RoleEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

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
import java.util.regex.Pattern;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("UserController")
@RequestMapping(value = "/user")
public class UserController {
    
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private static final Pattern EMAIL_REGEX = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$");
    
    
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private TradeHistoryService historyService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SimpleDateFormat requestDateFormat;

    @Autowired
    private DecimalFormat decimalFormat;
    

    @RequestMapping(method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView dashboard(final ModelAndView modelAndView,
                                  final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());

        modelAndView.addObject("format", decimalFormat);
        modelAndView.addObject("genres", genreService.findAll());
        modelAndView.addObject("user", user);

        modelAndView.setViewName("dashboard");
        
        return modelAndView;
    }

    @RequestMapping(value = "/boughtAlbums", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView albums(final ModelAndView modelAndView,
                               final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());
        
        final Map<String, List<Album>> map = new LinkedHashMap<>();
        
        for (final Album album : user.getAlbums()) {
            album.setIsBought(true);
            final Author author = album.getAuthor();
            if (map.containsKey(author.getName())) {
                map.get(author.getName()).add(album);
            } else {
                final List<Album> albums = new ArrayList<>();
                albums.add(album);
                map.put(author.getName(), albums);
            }
        }
        
        modelAndView.addObject("dataMap", map);
        modelAndView.addObject("format", decimalFormat);
        modelAndView.addObject("genres", genreService.findAll());
        modelAndView.addObject("user", user);
        
        modelAndView.setViewName("index");
        return modelAndView;
    }
    
    @RequestMapping(method = RequestMethod.POST,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto addUser(@RequestBody final UserDto newUser) {
        
        if (!EMAIL_REGEX.matcher(newUser.getEmail()).matches()) {
            throw new RequestDataException("Неверно введен email.");
        }
        
        final User user = new User(newUser.getLogin(), passwordEncoder.encode(newUser.getPass()), newUser.getEmail());
        
        final Set<Role> roles = new HashSet<>();
        
        final Role role = roleService.findByName(RoleEnum.USER.getRole());
        role.getUsers().add(user);
        roles.add(role);
        
        user.setRoles(roles);
        
        userService.save(user);
        return new ResponseMessageDto(HttpStatus.OK.value(), "Регистрация прошла успешно.");
    }

    @RequestMapping(method = RequestMethod.PUT,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto changeUserData(final @RequestBody UserDto data,
                                             final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());
        
        if (data.getEmail()!= null) {
            if (userService.findByEmail(data.getEmail()) != null) {
                throw new EmailAlreadyExistsException("Пользователь с email " + data.getEmail() + " уже существует.");
            } else {
                if (!EMAIL_REGEX.matcher(data.getEmail()).matches()) {
                    throw new RequestDataException("Неверно введен email.");
                }
                user.setEmail(data.getEmail());
                userService.update(user);
            }
        }
        
        if (data.getPass() != null) {
            user.setPassword(data.getPass());
            userService.update(user);
        }
        
        return new ResponseMessageDto(HttpStatus.OK.value(), "Обновление данных прошло успешно.");
    }

    @RequestMapping(value = "/addMoney", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDto addMoney(@RequestParam("value") final String value,
                            final Authentication authentication) {
        User user = userService.findByLogin(authentication.getName());
        final BigDecimal bdValue;
        try {
            bdValue = new BigDecimal(value);
            if (bdValue.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            log.warn("Не удалось понолнить счет. Пользователь {} неверно ввел сумму ({}).", user.getLogin(), value);
            throw new RequestDataException("Некорректно задана сумма.");
        }
        
        user = userService.addCash(user, bdValue);
        return new UserDto(decimalFormat.format(user.getWallet()));
    }

    @RequestMapping(value = "/discountMoney", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDto discountMoney(@RequestParam("value") final String value, final Authentication authentication) {
        
      User user = userService.findByLogin(authentication.getName());

       final BigDecimal bdValue;
        try {
            bdValue = new BigDecimal(value);
            if (bdValue.compareTo(BigDecimal.ZERO) <= 0) {
                throw new RuntimeException();
            }
        } catch (Exception ex) {
            log.warn("Не удалось вывести деньги. Пользователь {} неверно ввел сумму ({}).", user.getLogin(), value);
            throw new RequestDataException("Некорректно задана сумма.");
        }
       
       user = userService.discountCash(user, bdValue);
       
       return new UserDto(decimalFormat.format(user.getWallet()));
        
    }

    @RequestMapping(value = "/tradehistory", method = RequestMethod.GET, 
                    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<TradeHistoryDto> getTradeHistory(@RequestParam("from") final String from,
                                                 @RequestParam("to") final String to,
                                                 final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());
       
        try {
            final Date fromDate = requestDateFormat.parse(from),
                         toDate = requestDateFormat.parse(to);

            final List<TradeHistory> tradeHistories = historyService.findBetweenDays(user, fromDate, toDate);
            if (tradeHistories == null || tradeHistories.isEmpty()) {
                throw new ResourceNotFoundException("Ничего не найдено.");
            }
            
            return getTHDto(tradeHistories);
        } catch (ParseException ex) {
            log.warn("Задан неправельный формат даты.", ex);
            throw new RequestDataException("Задан неправельный формат даты.");
        }
        
    }
    
    
    private List<TradeHistoryDto> getTHDto(final List<TradeHistory> ths) {
        
        final List<TradeHistoryDto> result = new ArrayList<>();
        
        final DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(2);
        df.setGroupingUsed(false);
        
        for (final TradeHistory th : ths) {
            final TradeHistoryDto thDto = new TradeHistoryDto();
            
            thDto.setDatetime(th.getDatetime());
            thDto.setPrice(df.format(th.getPrice()));
            thDto.setAction(th.getAction().getDesc());
            
            final Album album = th.getAlbum();
            if (album != null) {
                thDto.setAlbum(album.getTitle());
            }
            
            result.add(thDto);
        }
        
        return result;
    }
    
    
}
