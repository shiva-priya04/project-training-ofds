package com.ofds.security;

import com.ofds.entity.User;
import com.ofds.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService
implements UserDetailsService {

    @Autowired
    UserRepository repository;

    @Override
    public UserDetails loadUserByUsername
            (String username)
            throws UsernameNotFoundException {

        User user =
                repository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"));

        return org.springframework.security.core.userdetails
                .User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole())
                .build();
    }
}
