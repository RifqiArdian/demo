package com.example.demo.infrastructure.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key secretKey;
    private static final long EXPIRATION_TIME = 86400000;

    public JwtService(@Value("${jwt.secret:}") String jwtSecret) {
        if (jwtSecret != null && !jwtSecret.isBlank()) {
            byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
            this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        } else {
            // fallback (ingat: ini membuat token invalid setelah restart)
            this.secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        }
    }

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}