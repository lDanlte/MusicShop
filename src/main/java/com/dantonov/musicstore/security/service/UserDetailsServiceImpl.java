package com.dantonov.musicstore.security.service;

import com.dantonov.musicstore.entity.Role;
import com.dantonov.musicstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author denis.antonov
 * @since 19.03.17.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    protected UserService userService;


    @Autowired
    public void setUserService(final UserService userService) {
        this.userService = userService;
    }


    @Override
    public UserDetails loadUserByUsername(final String s) throws UsernameNotFoundException {
        final com.dantonov.musicstore.entity.User user = userService.findByLogin(s);
        if (user == null) {
            throw new UsernameNotFoundException("User is not found");
        }
        return buildSpringSecurityUser(user, buildAuthorities(user.getRoles()));
    }


    private User buildSpringSecurityUser(final com.dantonov.musicstore.entity.User user,
                                         final List<GrantedAuthority> authorities) {
        return new User(user.getLogin(), user.getPassword(), authorities);
    }

    private List<GrantedAuthority> buildAuthorities(final Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getRole().toLowerCase()))
                .collect(Collectors.toList());
    }
}
