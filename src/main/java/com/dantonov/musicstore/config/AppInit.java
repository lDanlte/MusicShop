
package com.dantonov.musicstore.config;

import com.dantonov.musicstore.security.config.SecurityConfig;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.SessionTrackingMode;
import java.util.EnumSet;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
public class AppInit extends AbstractAnnotationConfigDispatcherServletInitializer {
    
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{ DataConfig.class, AppConfig.class, SecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{ WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{ "/" };
    }
    
     @Override
    protected void registerDispatcherServlet(final ServletContext servletContext) {
        super.registerDispatcherServlet(servletContext);
        servletContext.setSessionTrackingModes(EnumSet.of(SessionTrackingMode.COOKIE));
    }
    
    @Override
    protected Filter[] getServletFilters() {
       return new Filter[]{ new OpenEntityManagerInViewFilter() };
    }

}
