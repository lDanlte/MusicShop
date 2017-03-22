
package com.dantonov.musicstore.config;

import com.fasterxml.jackson.core.JsonEncoding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.accept.ContentNegotiationManager;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
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

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Denis Antonov (den007230@gmail.com)
 */
@Configuration
@ComponentScan(basePackages = {"com.dantonov.musicstore.controller"})
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    private static final int LONG_CACHE_PERIOD_IN_DAYS = 30;
    private static final int CACHE_PERIOD_IN_DAYS = 5;

    @Resource
    private Environment env;
    
    
    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        final Map<String, MediaType> map = new HashMap<>();
        
        map.put("html", MediaType.TEXT_HTML);
        map.put("json", MediaType.APPLICATION_JSON);
        
        configurer.mediaTypes(map);
    }
    
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/lib/**")
                .addResourceLocations("/resources/lib/")
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(CACHE_PERIOD_IN_DAYS));
        registry
                .addResourceHandler("/image/**")
                .addResourceLocations("/resources/image/")
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(LONG_CACHE_PERIOD_IN_DAYS));
        registry
                .addResourceHandler("/resource/*/cover*")
                .addResourceLocations(env.getRequiredProperty("storage.path"))
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(LONG_CACHE_PERIOD_IN_DAYS));
        registry
                .addResourceHandler("/resource/*/*/cover*")
                .addResourceLocations(env.getRequiredProperty("storage.path"))
                .setCachePeriod((int) TimeUnit.DAYS.toSeconds(LONG_CACHE_PERIOD_IN_DAYS));
    }

    
    @Bean
    public ViewResolver contentNegotiationViewResolver(final ContentNegotiationManager manager) {
        final ContentNegotiatingViewResolver viewResolverContent = new ContentNegotiatingViewResolver();
        viewResolverContent.setContentNegotiationManager(manager);

        final List<ViewResolver> viewResolvers = new ArrayList<>();
        
        viewResolvers.add(jsonViewResolver());
        viewResolvers.add(jspViewResolver());
        
        viewResolverContent.setViewResolvers(viewResolvers);
        
        return viewResolverContent;
    }
    
    @Bean
    public ViewResolver jsonViewResolver() {
        return (String viewName, Locale locale) -> {
            final MappingJackson2JsonView view = new MappingJackson2JsonView();
            view.setPrettyPrint(true);
            view.setEncoding(JsonEncoding.UTF8);
            return view;
        };
    }
    
    @Bean
    public ViewResolver jspViewResolver() {
        final InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        
        resolver.setPrefix("/resources/view/");
        resolver.setSuffix(".jsp");
        resolver.setViewClass(JstlView.class);
        resolver.setContentType("text/html;charset=UTF-8");
        
        return resolver;
    }
        
    @Bean
    public CommonsMultipartResolver multipartResolver() {
        final CommonsMultipartResolver resolver = new CommonsMultipartResolver();

        final long maxFileSize = Long.parseLong(env.getRequiredProperty("fileUpload.maxFileSize"));
        final int maxInMemorySize = Integer.parseInt(env.getRequiredProperty("fileUpload.maxInMemorySize"));
        
        resolver.setMaxUploadSizePerFile(maxFileSize);
        resolver.setMaxInMemorySize(maxInMemorySize);
        resolver.setDefaultEncoding("utf-8");
        
        return resolver;
    }
    
    
    @Override
    public void configureDefaultServletHandling(final DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }


}
