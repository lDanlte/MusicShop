package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.UserDto;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.inspector.AuthInspector;
import com.dantonov.musicstore.service.AuthService;
import com.dantonov.musicstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */

@Deprecated
public class AuthController {
    
    private static final DecimalFormat DEC_FORMAT = new DecimalFormat();
    static {
        DEC_FORMAT.setMaximumFractionDigits(2);
        DEC_FORMAT.setMinimumFractionDigits(2);
        DEC_FORMAT.setGroupingUsed(false);
    }

    @Autowired
    private  AuthService authService;
    
    @Autowired
    private UserService userService;

    

    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public UserDto login(@RequestParam("login") String username,
                         @RequestParam("pass") String password,
                         HttpServletResponse response) {

        User user = userService.findByLogin(username);
        if (user == null || !password.equals(user.getPassword())) {
            throw new RequestDataException("Неверный логин и/или пароль");
        }

        authService.login(user, response);
        return new UserDto(user.getLogin(), DEC_FORMAT.format(user.getWallet()));
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        User user = (User) request.getSession().getAttribute(AuthInspector.USER_ATTRIBUTE);
        if (user != null) {
            authService.logout(request, response, user);
        }
        return "redirect:/";
    }
    
    
}
