package com.reviewtrungtam.webapp.user.service;

import com.reviewtrungtam.webapp.user.core.UserDetailsFactory;
import com.reviewtrungtam.webapp.user.entity.User;
import com.reviewtrungtam.webapp.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    private UserDetailsFactory userDetailsFactory;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, UserDetailsFactory userDetailsFactory) {
        this.userRepository = userRepository;
        this.userDetailsFactory = userDetailsFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUserName(s);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException(s);
        }
        return userDetailsFactory.create(user.get());
    }

    public PasswordEncoder getPasswordEncoder() {
        if (passwordEncoder == null) {
            passwordEncoder = new BCryptPasswordEncoder();
        }
        return passwordEncoder;
    }
}
