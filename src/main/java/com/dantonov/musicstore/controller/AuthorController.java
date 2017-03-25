package com.dantonov.musicstore.controller;

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
import com.dantonov.musicstore.service.AuthorService;
import com.dantonov.musicstore.service.GenreService;
import com.dantonov.musicstore.service.MongoDataStorageService;
import com.dantonov.musicstore.service.RoleService;
import com.dantonov.musicstore.service.UserService;
import com.dantonov.musicstore.util.RoleEnum;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("AuthorController")
@RequestMapping("/author")
public class AuthorController {
    
    private static final Logger log = LoggerFactory.getLogger(AuthorController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthorService authorService;

    @Autowired
    private GenreService genreService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private  DecimalFormat decimalFormat;

    @Autowired
    private MongoDataStorageService storageService;



    @RequestMapping(value = "/{name}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView getAuthor(@PathVariable("name") final  String authorName,
                                  final ModelAndView modelAndView,
                                  final Authentication authentication) {

        final User user = (authentication == null)
                ? null
                : userService.findByLogin(authentication.getName());
        
        final Author author = authorService.findByName(authorName);
        
        if (author == null) {
            throw new PageNotFoundException();
        }
        
        modelAndView.addObject("pageContextStr", "author");
        
        final Map<String, List<Album>> map = new LinkedHashMap<>();
        
        final List<Album> albums = author.getAlbums();
        if (user != null) {
            setIsBought(albums, user);
        }
        
        map.put("Альбомы", albums);
        modelAndView.addObject("dataMap", map);
        
        modelAndView.addObject("author", author);
        modelAndView.addObject("format", decimalFormat);
        modelAndView.addObject("genres", genreService.findAll(true));
        modelAndView.addObject("user", user);
        
        modelAndView.setViewName("index");
        
        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto createAuthor(@RequestParam("author") final String authorDtoStr,
                                           @RequestParam("image") final MultipartFile file) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final AuthorDto authorDto = mapper.readValue(authorDtoStr, AuthorDto.class);

            if (!MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())) {
                log.warn("Ошибка при добавлени альбома. Неверный формат обложки.");
                throw  new RequestDataException("Тип обложки должен быть .jpg");
            }
            
            if (authorService.findByName(authorDto.getName()) != null) {
                throw new RequestDataException("Группа с названием " + authorDto.getName() + " уже существует.");
            }
            
            final User newUser = createUser(authorDto.getUser());
            final Author newAuthor = createAuthor(authorDto, newUser);
            newUser.setAuthor(newAuthor);
            
            final Set<Role> roles = new HashSet<>();
            Role role = roleService.findByName(RoleEnum.USER.getRole());
            role.getUsers().add(newUser);
            roles.add(role);
            role = roleService.findByName(RoleEnum.AUTHOR.getRole());
            role.getUsers().add(newUser);
            roles.add(role);
            newUser.setRoles(roles);

            final GridFSInputFile cover = storageService.save(
                    file.getInputStream(),
                    getFileName(newAuthor.getName(), file),
                    file.getContentType(),
                    null
            );
            newAuthor.setCoverId(cover.getId().toString());
            userService.save(newUser);
            
            return new ResponseMessageDto(HttpStatus.OK.value(), "Группа " + authorDto.getName() + " успешно добавлена.");
        } catch (final IOException ex) {
            log.warn("Ошибка при добавлении автора", ex);
            throw new RequestDataException("Ошибка при добавлении группы.");
        }
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto updateAuthor(@RequestParam(value = "desc", required = false) final String desc,
                                           @RequestParam(value = "cover") final MultipartFile file,
                                           @PathVariable("name") final String authorName,
                                           final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());

        if (!MediaType.IMAGE_JPEG_VALUE.equals(file.getContentType())) {
            log.warn("Ошибка при добавлени альбома. Неверный формат обложки.");
            throw  new RequestDataException("Тип обложки должен быть .jpg");
        }
        try {
            final Author author = authorService.findByName(authorName);
            if (!user.getAuthor().equals(author)) {
                log.warn("Ошибка при обновлении даннах группы. Польователь не является владельцем этой группы.");
                throw new UnauthorizedResourceException("Ошибка при обновлении даннах группы. Польователь не является владельцем этой группы.");
            }

            final GridFSInputFile cover = !file.isEmpty()
                    ? storageService.save(file.getInputStream(), getFileName(authorName, file), file.getContentType(), null)
                    : null;

            if (desc != null) {
                author.setDesc(desc);
                if (cover != null) {
                    author.setCoverId(cover.getId().toString());
                }
                authorService.save(author);
            }
        } catch (IOException ex) {
            log.warn("Ошибка при обновлении даннах группы", ex);
            throw new RequestDataException("Ошибка при обновлении даннах группы.");
        }
        return new ResponseMessageDto(HttpStatus.OK.value(), "Группа " + authorName + " успешно обновлена.");
    }

    @RequestMapping(value = "/{name}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseMessageDto updateAuthor(@RequestParam(value = "desc", required = false) final String desc,
                                           @PathVariable("name") final String authorName,
                                           final Authentication authentication) {

        final User user = userService.findByLogin(authentication.getName());

        final Author author = authorService.findByName(authorName);
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

    @RequestMapping(value = "/{name}/cover.jpg", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public ResponseEntity<InputStreamResource> getCover(@PathVariable("name") final String authorName,
                                                        final WebRequest webRequest) {
        final Author author = authorService.findByName(authorName);
        if (author == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        final GridFSDBFile cover = storageService.findById(author.getCoverId());
        if (cover == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (webRequest.checkNotModified(cover.getUploadDate().getTime())) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

        return ResponseEntity.ok()
                .contentLength(cover.getLength())
                .lastModified(cover.getUploadDate().getTime())
                .contentType(MediaType.IMAGE_JPEG)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.DAYS))
                .body(new InputStreamResource(cover.getInputStream()));
    }

    
    
    private User createUser(final UserDto userDto) {
        final User user = new User();
        user.setLogin(userDto.getLogin());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPass()));
        
        final Set<Role> roles = new HashSet<>();
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
    
    private Author createAuthor(final AuthorDto authorDto, final User user) {
        return new Author(authorDto.getName(), authorDto.getDesc(), user);
    }
    
    private void setIsBought(final List<Album> albums, final User user) {
        for (final Album album : albums) {
            if (user.hasAlbum(album)) {
                album.setIsBought(true);
            }
        }
    }

    private String getFileName(final String authorName, final MultipartFile cover) {
        return authorName + " author cover" + cover.getOriginalFilename().substring(cover.getOriginalFilename().lastIndexOf('.'));
    }
}
