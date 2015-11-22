package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.ResponseMessageDto;
import com.dantonov.musicstore.exception.AlreadyBoughtException;
import com.dantonov.musicstore.exception.AppSQLException;
import com.dantonov.musicstore.exception.EmailAlreadyExistsException;
import com.dantonov.musicstore.exception.LoginAlreadyExistsException;
import com.dantonov.musicstore.exception.NotEnoughMoneyException;
import com.dantonov.musicstore.exception.PageNotFoundException;
import com.dantonov.musicstore.exception.RequestDataException;
import com.dantonov.musicstore.exception.ResourceNotFoundException;
import com.dantonov.musicstore.exception.UnauthorizedPageException;
import com.dantonov.musicstore.exception.UnauthorizedResourceException;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@ControllerAdvice
public class GlobalControllerExceptionHandler {

    public static final String DEFAULT_ERROR_VIEW = "404";
    
    @ExceptionHandler(value = PageNotFoundException.class)
    public String notFoundHandler() {
        return DEFAULT_ERROR_VIEW;
    }
    
    @ExceptionHandler(value = {NotEnoughMoneyException.class,
                               AlreadyBoughtException.class,
                               RequestDataException.class,
                               EmailAlreadyExistsException.class,
                               LoginAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseMessageDto badRequestHandler(Exception e) {
        return new ResponseMessageDto(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
    
    @ExceptionHandler(value = UnauthorizedResourceException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseMessageDto unauthorizedResourceHandler(Exception e) {
        return new ResponseMessageDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
    }
    
    @ExceptionHandler(value = UnauthorizedPageException.class)
    public String unauthorizedPageHandler(RedirectAttributes redirectAttributes,
                                          HttpSession httpSession,
                                          Exception e) {
        
        redirectAttributes.addFlashAttribute("redirectCause", e.getMessage());
        return "redirect:/";
    }
    
    @ExceptionHandler(value = ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseMessageDto resourceNotFoundHandler(Exception e) {
        return new ResponseMessageDto(HttpStatus.NOT_FOUND.value(), e.getMessage());
    }
    
    @ExceptionHandler(value = AppSQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseMessageDto appSQLExceptionHandler(Exception e) {
        return new ResponseMessageDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage());
    }
    

}
