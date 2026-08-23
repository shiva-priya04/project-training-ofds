package com.ofds.controller;

import com.ofds.dto.LoginRequest;
import com.ofds.dto.LoginResponse;
import com.ofds.entity.Agent;
import com.ofds.entity.Customer;
import com.ofds.entity.User;
import com.ofds.repository.UserRepository;
import com.ofds.security.JwtUtil;
import com.ofds.service.AgentService;
import com.ofds.service.CustomerService;

import org.springframework.beans.factory.annotation.
Autowired;

import org.springframework.security.authentication.
AuthenticationManager;

import org.springframework.security.authentication.
UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.
PasswordEncoder;

import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    UserRepository repository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    AuthenticationManager manager;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    CustomerService customerService;

    @Autowired
    AgentService agentService;

    @PostMapping("/register")
    public String register(
            @RequestBody User user) {

        user.setRole(normalizeRole(user.getRole()));
        user.setPassword(
                encoder.encode(
                        user.getPassword()));

        repository.save(user);

        String role = normalizeRole(user.getRole());
        String uid = buildShortId(user.getUsername());

        if ("CUSTOMER".equals(role)) {
            Customer customer = new Customer();
            customer.setCustomerId("CUST" + uid);
            customer.setCustomerName(user.getUsername());
            customer.setCustomerEmail(user.getUsername() + "@ofds-customer.local");
            customer.setCustomerPhoneNo("9000000000");
            customer.setCustomerAddress("Not provided yet");
            try {
                customerService.addCustomer(customer);
            } catch (RuntimeException ex) {
                log.warn("Could not create Customer record for username '{}': {}", user.getUsername(), ex.getMessage());
            }
        } else if ("AGENT".equals(role)) {
            Agent agent = new Agent();
            agent.setAgentId("AGT" + uid);
            agent.setAgentName(user.getUsername());
            agent.setAgentPhoneNo("9000000000");
            try {
                agentService.saveAgent(agent);
            } catch (RuntimeException ex) {
                log.warn("Could not create Agent record for username '{}': {}", user.getUsername(), ex.getMessage());
            }
        }

        return "Registered";
    }

    private String buildShortId(String input) {
        int hash = Math.abs(input.hashCode());
        // Kept to 6 digits so "CUST" + id (10 chars) and "AGT" + id (9 chars)
        // both fit within the existing varchar(10) customerId/agentId columns.
        return String.format("%06d", hash % 1_000_000);
    }

    @PostMapping("/login")
    public LoginResponse login(
            @RequestBody LoginRequest request) {

        manager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()));

        String token = jwtUtil.generateToken(request.getUsername());

        String role = repository.findByUsername(request.getUsername())
                .map(User::getRole)
                .map(this::normalizeRole)
                .orElse("CUSTOMER");

        return new LoginResponse(token, request.getUsername(), role);
    }

    private String normalizeRole(String role) {
        if (role == null || role.isBlank()) {
            return "CUSTOMER";
        }
        return role.trim().replaceFirst("(?i)^ROLE_", "").toUpperCase();
    }
}