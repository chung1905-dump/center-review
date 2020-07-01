package com.reviewtrungtam.webapp.user.core;

import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class UserDetailsFactory {
    public UserDetails create(com.reviewtrungtam.webapp.user.entity.User user) {
        UserDetails u = new UserDetails(user.getUserName(), user.getPassword(), parseAuthorities(user.getRoles()));
        u.setUser(user);

        return u;
    }

    private Set<GrantedAuthority> parseAuthorities(String roles) {
        Set<GrantedAuthority> set = new HashSet<>();
        JsonParser jsonParser = new BasicJsonParser();
        List<Object> roleList = jsonParser.parseList(roles);
        for (Object role : roleList) {
            set.add(new SimpleGrantedAuthority(role.toString()));
        }

        return set;
    }
}
