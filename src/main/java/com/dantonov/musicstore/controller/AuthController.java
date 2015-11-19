package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.ResponseMessageDto;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.exception.ResourceNotFoundException;
import com.dantonov.musicstore.service.AuthService;
import com.dantonov.musicstore.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Controller
public class AuthController {

    @Autowired
    private  AuthService authService;
    
    @Autowired
    private UserService userService;

    
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public ResponseMessageDto login(@RequestParam("login") String username,
                               @RequestParam("pass") String password,
                               HttpServletResponse response) {
       
        User user = userService.findByLogin(username);
        if (user == null || !password.equals(user.getPassword())) {
            throw new RequestDataException("Неверный логин и/или пароль");
        }

        authService.login(user, response);
        return new ResponseMessageDto(HttpStatus.OK.value(), "Успешный вход.");
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return "redirect:/";
    }
    
    
}
