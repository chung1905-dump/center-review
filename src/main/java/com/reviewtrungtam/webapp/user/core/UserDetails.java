package com.reviewtrungtam.webapp.user.core;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetails extends User {
    private com.reviewtrungtam.webapp.user.entity.User user;

    UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public com.reviewtrungtam.webapp.user.entity.User getUser() {
        return user;
    }

    public void setUser(com.reviewtrungtam.webapp.user.entity.User user) {
        this.user = user;
    }
}
