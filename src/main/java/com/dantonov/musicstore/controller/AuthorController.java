package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.AuthorDto;
import com.dantonov.musicstore.service.DataManagementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller("AuthorController")
@RequestMapping("/author")
public class AuthorController {
    
    @Autowired
    private DataManagementService dataService;

    
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public void createUser(@RequestParam("author") String authorDtoStr, 
                           @RequestParam("image") MultipartFile file) {
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            AuthorDto authorDto = mapper.readValue(authorDtoStr, AuthorDto.class);
            
            dataService.saveAuthorCover(authorDto.getName(), file);
        } catch (IOException ex) {
            Logger.getLogger(AuthorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
