
package com.dantonov.musicstore.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = {"com.dantonov.musicstore.controller", "com.dantonov.musicstore.service"})
@EnableWebMvc
@PropertySource("classpath:app.properties")
public class WebConfig extends WebMvcConfigurerAdapter {
    
    @Resource
    private Environment env;
    
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        Map<String, MediaType> map = new HashMap<>();
        
        map.put("html", MediaType.TEXT_HTML);
        map.put("json", MediaType.APPLICATION_JSON);
        
        configurer.mediaTypes(map);
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/lib/**").addResourceLocations("/WEB-INF/lib/");
        registry.addResourceHandler("/image/**").addResourceLocations("/WEB-INF/image/");
        registry.addResourceHandler("/resource/**").addResourceLocations(env.getRequiredProperty("storage.path"));
    }
    
    @Bean
    public ViewResolver contentNegotiationViewResolver(ContentNegotiationManager manager) {
        ContentNegotiatingViewResolver viewResolverContent = new ContentNegotiatingViewResolver();
        viewResolverContent.setContentNegotiationManager(manager);
        
        List<ViewResolver> viewResolvers = new ArrayList<>();
        
        viewResolvers.add(jsonViewResolver());
        viewResolvers.add(jspViewResolver());
        
        viewResolverContent.setViewResolvers(viewResolvers);
        
        return viewResolverContent;
    }
    
    @Bean
    public ViewResolver jsonViewResolver() {
        return (String viewName, Locale locale) -> {
            MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setPrettyPrint(true);
            return view;
        };
    }
    
    @Bean
    public ViewResolver jspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        
        resolver.setPrefix("/WEB-INF/view/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        resolver.setContentType("text/html;charset=UTF-8");
        
        return resolver;
    }
    
    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

}
