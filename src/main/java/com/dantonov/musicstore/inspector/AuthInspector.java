package com.dantonov.musicstore.inspector;

import com.dantonov.musicstore.annotation.Secured;
import com.dantonov.musicstore.entity.User;
import com.dantonov.musicstore.service.AuthService;
import com.dantonov.musicstore.service.GenreService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author Antonov Denis (den007230@gmail.com)
 */
public class AuthInspector extends HandlerInterceptorAdapter {
    
    public static final String USER_ATTRIBUTE = "user";

    
    @Autowired
    protected AuthService authService;
    
    @Autowired
    protected GenreService genreService;

    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = authService.getUser(request);
        
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Secured secured = handlerMethod.getMethodAnnotation(Secured.class);
            if (secured != null) {
                Class<?> returnClass = handlerMethod.getReturnType().getClass();
                if (user == null) {
                    if (isResponseBody(returnClass)) {
                        response.sendRedirect(request.getContextPath() + "/unauthorizedUserBody");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/unauthorizedUserPage");
                    }
                    return false; //maybe not allowed
                }
                if (!user.hasRole(secured.role().getRole())) {
                    if (isResponseBody(returnClass)) {
                        response.sendRedirect(request.getContextPath() + "/userHasNoRoleBody");
                    } else {
                        response.sendRedirect(request.getContextPath() + "/userHasNoRolePage");
                    }
                    return false;
                }
            }
        }
        
        request.getSession(true).setAttribute(USER_ATTRIBUTE, user);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        modelAndView.addObject("genres", genreService.findAll());
        modelAndView.addObject("user", request.getSession().getAttribute(USER_ATTRIBUTE));
        request.getSession().removeAttribute(USER_ATTRIBUTE);
    }
    
    
    private boolean isResponseBody(Class<?> returnClass) {
        return !(!returnClass.isAnnotationPresent(ResponseBody.class) ||
                  returnClass.isAssignableFrom(String.class) || 
                  returnClass.isAssignableFrom(ModelAndView.class) ||
                  returnClass.isAssignableFrom(View.class)); 
    }
}
