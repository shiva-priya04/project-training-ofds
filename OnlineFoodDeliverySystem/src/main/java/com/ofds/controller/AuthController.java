package com.ofds.controller;

import com.ofds.dto.LoginRequest;
import com.ofds.entity.User;
import com.ofds.repository.UserRepository;
import com.ofds.security.JwtUtil;

import org.springframework.beans.factory.annotation.
Autowired;

import org.springframework.security.authentication.
AuthenticationManager;

import org.springframework.security.authentication.
UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.
PasswordEncoder;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    JwtUtil jwtUtil;

    @PostMapping("/register")
    public String register(
            @RequestBody User user) {

        user.setPassword(
                encoder.encode(
                        user.getPassword()));

        repository.save(user);

        return "Registered";
    }

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request) {

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        return jwtUtil.generateToken(
                request.getUsername());
    }
}