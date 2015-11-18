package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.service.AuthService;
import com.dantonov.musicstore.service.UserService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    @ResponseBody
    public String performLogin(@RequestParam("login") String username,
                               @RequestParam("pass") String password,
                               HttpServletResponse response) {
       
        User user = userService.findByLogin(username);
        if (user == null) {
            return "User not found.";
        }
        if (!password.equals(user.getPassword())) {
            return "Invalid pass";
        }
        authService.login(user, response);
        return "Login.";
    }
    
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return "redirect:/";
    }
}
