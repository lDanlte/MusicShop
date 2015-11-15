package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.AuthorDto;
import com.dantonov.musicstore.dto.UserDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Genre;
import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.service.AuthService;
import com.dantonov.musicstore.service.AuthorService;
import com.dantonov.musicstore.service.DataManagementService;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.RoleService;
import com.dantonov.musicstore.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("AuthorController")
@RequestMapping("/author")
public class AuthorController {
    
    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);
    private static final String USER_ROLE = "User";
    private static final String AUTHOR_ROLE = "Author";
    private static final String ADMIN_ROLE = "Admin";
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat();
    static {
        DEC_FORMAT.setMaximumFractionDigits(2);
        DEC_FORMAT.setMinimumFractionDigits(2);
        DEC_FORMAT.setGroupingUsed(false);
    }

    
    @Autowired
    private DataManagementService dataService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private GenreService genreService;
    
    @Autowired
    private RoleService roleService;
    
    @Autowired
    private AuthorService authorService;
    
    @Autowired
    private AuthService authService;
    
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    public ModelAndView getAuthor(@PathVariable("name") String authorName,
                                  ModelAndView modelAndView,
                                  HttpServletRequest request) {
        
        User user = authService.getUser(request);
        if (user != null) {
            modelAndView.addObject("user", user);
        }
        
        Author author = authorService.findByName(authorName);
        if (author == null) {
            modelAndView.setViewName("redirect:/");
            return modelAndView;
        }
        
        modelAndView.addObject("pageContextStr", "author");
        
        List<Genre> genres = genreService.findAll();
        modelAndView.addObject("genres", genres);
        
        Map<String, List<Album>> map = new LinkedHashMap<>();
        map.put("Альбомы", author.getAlbums());
        modelAndView.addObject("dataMap", map);
        
        modelAndView.addObject("author", author);
        modelAndView.addObject("format", DEC_FORMAT);
        
        modelAndView.setViewName("index");
        
        return modelAndView;
    }
    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createAuthor(@RequestParam("author") String authorDtoStr, 
                             @RequestParam("image") MultipartFile file,
                             HttpServletRequest request) {
        
        User user = authService.getUser(request);
        if (user == null) {
            return;
        }
        if (!user.hasRole(ADMIN_ROLE)) {
            return;
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            AuthorDto authorDto = mapper.readValue(authorDtoStr, AuthorDto.class);
            
            User newUser = createUser(authorDto.getUser());
            newUser.setAuthor(createAuthor(authorDto, newUser));
            userService.save(newUser);
            
            dataService.saveAuthorCover(authorDto.getName(), file);
        } catch (IOException ex) {
            log.warn("Ошибка при добавлении автора", ex);
        } catch (Exception ex) {
            log.warn("Ошибка при добавлении автора", ex);
        }
    }
    
    @RequestMapping(value = "/{name}/update", method = RequestMethod.PUT)
    public void updateAuthor(@RequestParam(value = "desc", required = false) String desc, 
                             @RequestParam(value = "cover") MultipartFile file,
                             @PathVariable("name") String authorName,
                             HttpServletRequest request) {
        
        User user = authService.getUser(request);
        if (user == null) {
            log.warn("Ошибка при обновлении даннах группы. Пользователь не авторизирован.");
            return;
        }
        if (!user.hasRole(AUTHOR_ROLE)) {
            log.warn("Ошибка при обновлении даннах группы. Пользователь не обладает правами автора.");
            return;
        }
        
        try {
            Author author = authorService.findByName(authorName);
            if (!user.getAuthor().equals(author)) {
                log.warn("Ошибка при обновлении даннах группы. Польователь не является владельцем этой группы.");
                return;
            }
            if (desc != null) {
                author.setDesc(desc);
                authorService.save(author);
            }
            
            if (file != null && !file.isEmpty()) {
                dataService.saveAuthorCover(authorName, file);
            }
            
        } catch (Exception ex) {
            log.warn("Ошибка при обновлении даннах группы", ex);
        }
    }
    
    
    
    @RequestMapping(value = "/{name}/update", method = RequestMethod.POST)
    public void updateAuthor(@RequestParam(value = "desc", required = false) String desc,
                             @PathVariable("name") String authorName) {
        try {
            Author author = authorService.findByName(authorName);
            
            if (desc != null) {
                author.setDesc(desc);
                authorService.save(author);
            }
            
            
        } catch (Exception ex) {
            log.warn("Ошибка при обновлении даннах группы", ex);
        }
    }
    
    
    private User createUser(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPass());
        
        Set<Role> roles = new HashSet<>();
        Role role = roleService.findByName(USER_ROLE);
        role.getUsers().add(user);
        roles.add(role);
        role = roleService.findByName(AUTHOR_ROLE);
        role.getUsers().add(user);
        roles.add(role);
        roles.add(role);
        user.setRoles(roles);
        
        return user;
    }
    
    private Author createAuthor(AuthorDto authorDto, User user) {
        return new Author(authorDto.getName(), authorDto.getDesc(), user);
    }
}
