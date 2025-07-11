package com.task.TeamManager.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.Set;


@Service
public class JwtService {

    // Keep your secret key long, secure, and ideally in application.properties or env vars
    private final Key key;
    private final long jwtExpirationMs;

    public JwtService(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") long jwtExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
        this.jwtExpirationMs = jwtExpirationMs;
    }

    /**
     * Generates a JWT token with username and roles.
     */
    public String generateJwtToken(String username, Set<String> roles) {
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extracts username (subject) from JWT.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Validates the JWT token and ensures it's not expired or tampered.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException e) {
            System.err.println("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            System.err.println("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            System.err.println("JWT token is expired.");
        } catch (UnsupportedJwtException e) {
            System.err.println("JWT token is unsupported.");
        } catch (IllegalArgumentException e) {
            System.err.println("JWT claims string is empty.");
        }
        return false;
    }
}
