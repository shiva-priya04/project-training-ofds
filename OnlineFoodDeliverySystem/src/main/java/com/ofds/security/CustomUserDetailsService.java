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

        String normalizedRole = normalizeRole(user.getRole());

        return org.springframework.security.core.userdetails
                .User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_" + normalizedRole)
                .build();
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "CUSTOMER";
        }
        return role.trim().replaceFirst("(?i)^ROLE_", "").toUpperCase();
    }
}
