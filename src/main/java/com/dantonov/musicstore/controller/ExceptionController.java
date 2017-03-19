package com.dantonov.musicstore.controller;

import com.dantonov.musicstore.dto.ResponseMessageDto;
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
    public ResponseMessageDto unauthorizedUserBodyHandler() {
         return new ResponseMessageDto(HttpStatus.UNAUTHORIZED.value(), "Для доступа к ресурсу необходимо авторизоваться.");
    }
    
    @RequestMapping(value = "/unauthorizedUserPage")
    public String unauthorizedUserPageHandler(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("redirectCause", "Для доступа к странице необходимо авторизоваться.");
        return "redirect:/";
    }

    @RequestMapping(value = "/failedToLogin")
    public String failedToLoginHandler(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("redirectCause", "Неверно введен логин и/или пароль");
        return "redirect:/";
    }
    
    @RequestMapping(value = "/userHasNoRoleBody")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseMessageDto userHasNoRoleBodyHandler() {
        return new ResponseMessageDto(HttpStatus.UNAUTHORIZED.value(), "Недостаточно прав для доступа к ресурсу.");
    }
    
    @RequestMapping(value = "/userHasNoRolePage")
    public String userHasNoRolePageHandler(final RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("redirectCause", "Недостаточно прав для доступа к странице.");
        return "redirect:/";
    }
    
    
    
    
}
