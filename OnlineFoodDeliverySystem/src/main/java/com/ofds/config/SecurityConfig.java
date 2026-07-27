package com.ofds.config;

import org.springframework.context.annotation.*;
import org.springframework.security.authentication.
AuthenticationManager;

import org.springframework.security.config.annotation.
authentication.configuration.
AuthenticationConfiguration;

import org.springframework.security.config.annotation.
web.builders.HttpSecurity;

import org.springframework.security.config.
http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.
BCryptPasswordEncoder;

import org.springframework.security.crypto.password.
PasswordEncoder;

import org.springframework.security.web.
SecurityFilterChain;

import org.springframework.security.web.
authentication.
UsernamePasswordAuthenticationFilter;

import com.ofds.security.JwtFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http.csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/auth/**")
                        .permitAll()

                        .requestMatchers("/restaurant/**")
                        .hasAnyRole("ADMIN")

                        .requestMatchers("/menu/**")
                        .hasRole("ADMIN")

                        .requestMatchers("/orders/**")
                        .hasAnyRole("CUSTOMER","ADMIN")

                        .requestMatchers("/payment/**")
                        .hasRole("CUSTOMER")

                        .requestMatchers("/agent/**")
                        .hasRole("AGENT")

                        .requestMatchers("/delivery/**")
                        .hasAnyRole("AGENT","ADMIN")
                        
                        .anyRequest()
                        .authenticated())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .addFilterBefore(
                        jwtFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}