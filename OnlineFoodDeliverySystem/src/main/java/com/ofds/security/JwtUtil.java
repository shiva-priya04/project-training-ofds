package com.ofds.security;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET =
            "OFDSPROJECTJWTSECRETKEY123456789123456";

    private Key getSignKey() {
        return Keys.hmacShaKeyFor(
                SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String username) {

        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 24))
                .signWith(getSignKey())
                .compact();
    }

    public String extractUsername(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((javax.crypto.SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    public boolean validateToken(String token,
                                 String username) {

        return username.equals(extractUsername(token));
    }
}