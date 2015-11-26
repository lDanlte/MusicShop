package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.annotation.Secured;
import com.dantonov.musicstore.dto.AuthorDto;
import com.dantonov.musicstore.dto.ResponseMessageDto;
import com.dantonov.musicstore.dto.UserDto;
import com.dantonov.musicstore.entity.Album;
import com.dantonov.musicstore.entity.Author;
import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.PageNotFoundException;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.exception.UnauthorizedResourceException;
import com.dantonov.musicstore.inspector.AuthInspector;
import com.dantonov.musicstore.service.AuthorService;
import com.dantonov.musicstore.service.DataManagementService;
import com.dantonov.musicstore.service.RoleService;
import com.dantonov.musicstore.service.UserService;
import com.dantonov.musicstore.util.RoleEnum;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
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
    private RoleService roleService;
    
    @Autowired
    private AuthorService authorService;
    
    
    
    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getAuthor(@PathVariable("name") String authorName,
                                  ModelAndView modelAndView,
                                  HttpServletRequest request) {
        
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
        
        Author author = authorService.findByName(authorName);
        
        if (author == null) {
            throw new PageNotFoundException();
        }
        
        modelAndView.addObject("pageContextStr", "author");
        
        Map<String, List<Album>> map = new LinkedHashMap<>();
        
        List<Album> albums = author.getAlbums();
        if (user != null) {
            setIsBought(albums, user);
        }
        
        map.put("Альбомы", albums);
        modelAndView.addObject("dataMap", map);
        
        modelAndView.addObject("author", author);
        modelAndView.addObject("format", DEC_FORMAT);
        
        modelAndView.setViewName("index");
        
        return modelAndView;
    }
    
    @Secured(role = RoleEnum.ADMIN)
    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto createAuthor(@RequestParam("author") String authorDtoStr, 
                                           @RequestParam("image") MultipartFile file) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AuthorDto authorDto = mapper.readValue(authorDtoStr, AuthorDto.class);
            
            if (authorService.findByName(authorDto.getName()) != null) {
                throw new RequestDataException("Группа с названием " + authorDto.getName() + " уже существует.");
            }
            
            User newUser = createUser(authorDto.getUser());
            newUser.setAuthor(createAuthor(authorDto, newUser));
            
            Set<Role> roles = new HashSet<>();
            Role role = roleService.findByName(RoleEnum.USER.getRole());
            role.getUsers().add(newUser);
            roles.add(role);
            role = roleService.findByName(RoleEnum.AUTHOR.getRole());
            role.getUsers().add(newUser);
            roles.add(role);
            newUser.setRoles(roles);
            
            userService.save(newUser);
            
            dataService.saveAuthorCover(authorDto.getName(), file);
            
            return new ResponseMessageDto(HttpStatus.OK.value(), "Группа" + authorDto.getName() + " успешно добавлена.");
                    
        } catch (RequestDataException e) {
            throw e;
        } catch (IOException ex) {
            log.warn("Ошибка при добавлении автора", ex);
            throw new RequestDataException("Ошибка при добавлении группы.");
        }
    }
    
    @Secured(role = RoleEnum.AUTHOR)
    @RequestMapping(value = "/{name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto updateAuthor(@RequestParam(value = "desc", required = false) String desc, 
                                            @RequestParam(value = "cover") MultipartFile file,
                                            @PathVariable("name") String authorName,
                                            HttpServletRequest request) {
        
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
        
        try {
            Author author = authorService.findByName(authorName);
            if (!user.getAuthor().equals(author)) {
                log.warn("Ошибка при обновлении даннах группы. Польователь не является владельцем этой группы.");
                throw new UnauthorizedResourceException("Ошибка при обновлении даннах группы. Польователь не является владельцем этой группы.");
            }
            if (desc != null) {
                author.setDesc(desc);
                authorService.save(author);
            }
            
            if (file != null && !file.isEmpty()) {
                dataService.saveAuthorCover(authorName, file);
            }
            
        } catch (IOException ex) {
            log.warn("Ошибка при обновлении даннах группы", ex);
            throw new RequestDataException("Ошибка при обновлении даннах группы.");
        }
        return new ResponseMessageDto(HttpStatus.OK.value(), "Группа " + authorName + " успешно обновлена.");
    }
    
    @Secured(role = RoleEnum.AUTHOR)
    @RequestMapping(value = "/{name}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto updateAuthor(@RequestParam(value = "desc", required = false) String desc,
                                           @PathVariable("name") String authorName,
                                           HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);

        Author author = authorService.findByName(authorName);
        if (!user.getAuthor().equals(author)) {
            log.warn("Ошибка при обновлении даннах группы. Польователь не является владельцем этой группы.");
            throw new UnauthorizedResourceException("Ошибка при обновлении даннах группы. Польователь не является владельцем этой группы.");
        }
        if (desc != null) {
            author.setDesc(desc);
            authorService.save(author);
        }
        return new ResponseMessageDto(HttpStatus.OK.value(), "Группа " + authorName + " успешно обновлена.");
    }
    
    
    private User createUser(UserDto userDto) {
        User user = new User();
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPass());
        
        Set<Role> roles = new HashSet<>();
        Role role = roleService.findByName(RoleEnum.USER.getRole());
        role.getUsers().add(user);
        roles.add(role);
        role = roleService.findByName(RoleEnum.AUTHOR.getRole());
        role.getUsers().add(user);
        roles.add(role);
        roles.add(role);
        user.setRoles(roles);
        
        return user;
    }
    
    private Author createAuthor(AuthorDto authorDto, User user) {
        return new Author(authorDto.getName(), authorDto.getDesc(), user);
    }
    
    private void setIsBought(List<Album> albums, User user) {
        for (Album album : albums) {
            if (user.hasAlbum(album)) {
                album.setIsBought(true);
            }
        }
    }
    
    
}
