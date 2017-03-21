package com.dantonov.musicstore.security.config;

import com.dantonov.musicstore.config.DataConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

/**
 * @author denis.antonov
 * @since 19.03.17.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@ComponentScan(basePackages = {"com.dantonov.musicstore.security.service", "com.dantonov.musicstore.service"})
@Import(DataConfig.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private static final int REMEMBER_ME_ALIVE_DAYS = 90;
    public static final String REMEMBER_ME_COOKIE_NAME = "at";

    private DataSource dataSource;
    private UserDetailsService userDetailsService;


    @Autowired
    public void setUserDetailsService(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Autowired
    public void setDataSource(final DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void configure(final WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers(
                "/lib/**",
                "/image/**",
                "/resource/**"
        );
    }

    protected void configure(final HttpSecurity http) throws Exception {

        http.authorizeRequests()

                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/unauthorizedUserBody",
                        "/unauthorizedUserPage",
                        "/userHasNoRoleBody",
                        "/userHasNoRolePage",
                        "/failedToLogin",
                        "/search",
                        "/category",
                        "/author/*",
                        "/author/*/album/*"
                ).permitAll()
                .antMatchers(HttpMethod.POST, "/user").permitAll()

                .antMatchers(
                        HttpMethod.GET,
                        "/user",
                        "/user/boughtAlbums",
                        "/user/tradehistory",
                        "/author/*/album/*/track/*"
                ).hasAuthority("user")
                .antMatchers(
                        HttpMethod.PUT,
                        "/user",
                        "/user/addMoney",
                        "/user/discountMoney",
                        "/author/*/album/*/buy"
                ).hasAuthority("user")

                .antMatchers(HttpMethod.POST, "/author/*", "/author/*/album").hasAuthority("author")
                .antMatchers(HttpMethod.PUT, "/author/*").hasAuthority("author")

                .antMatchers(HttpMethod.POST, "/author").hasAuthority("admin")
                .anyRequest().authenticated()

            .and()

                .formLogin()
                .defaultSuccessUrl("/", true)
                .loginProcessingUrl("/login")
                .usernameParameter("login")
                .passwordParameter("pass")
                .failureForwardUrl("/failedToLogin")
                .permitAll()

            .and()

                .rememberMe()
                .rememberMeParameter("remember-me")
                .tokenRepository(persistentTokenRepository())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(REMEMBER_ME_ALIVE_DAYS))
                .rememberMeCookieName(REMEMBER_ME_COOKIE_NAME)

            .and()

                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .invalidateHttpSession(true)
                .deleteCookies(REMEMBER_ME_COOKIE_NAME)
                .permitAll();


    }

    @Autowired
    public void registerGlobalAuthentication(final AuthenticationManagerBuilder authenticationBuilder) throws Exception {
        authenticationBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        final JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();

        jdbcTokenRepository.setDataSource(dataSource);
        jdbcTokenRepository.setCreateTableOnStartup(false);

        return jdbcTokenRepository;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
