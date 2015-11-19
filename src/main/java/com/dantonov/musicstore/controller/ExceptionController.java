package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.ResponseMessageDto;

import javax.servlet.http.HttpSession;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
@Controller
public class ExceptionController {

    @RequestMapping(value = "/unauthorizedUserBody")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseMessageDto unauthorizedUserBodyHendler() {
         return new ResponseMessageDto(HttpStatus.UNAUTHORIZED.value(), "Для доступа к ресурсу необходимо авторизоваться.");
    }
    
    @RequestMapping(value = "/unauthorizedUserPage")
    public String unauthorizedUserPageHendler(RedirectAttributes redirectAttributes, HttpSession httpSession) {
        redirectAttributes.addFlashAttribute("redirectCause", "Для доступа к странице необходимо авторизоваться.");
        return "redirect:/";
    }
    
    @RequestMapping(value = "/userHasNoRoleBody")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseMessageDto userHasNoRoleBodyHendler() {
        return new ResponseMessageDto(HttpStatus.UNAUTHORIZED.value(), "Недостаточно прав для доступа к ресурсу.");
    }
    
    @RequestMapping(value = "/userHasNoRolePage")
    public String userHasNoRolePageHendler(RedirectAttributes redirectAttributes, HttpSession httpSession) {
        redirectAttributes.addFlashAttribute("redirectCause", "Недостаточно прав для доступа к странице.");
        return "redirect:/";
    }
    
    
    
    
}
