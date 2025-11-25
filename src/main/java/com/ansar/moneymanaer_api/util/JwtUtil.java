package com.ansar.moneymanaer_api.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private long accessTokenExpiration; // 24 hrs

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenExpiration; // 7 days in milliseconds
    private Key signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    // ========================= ACCESS TOKEN ========================= //

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), accessTokenExpiration);
    }

    // ========================= REFRESH TOKEN ======================== //

    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(userDetails.getUsername(), refreshTokenExpiration);
    }

    // ========================= COMMON METHODS ======================= //

    private String generateToken(String username, long expirationMillis) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        try {
            return extractClaims(token).getExpiration().before(new Date());
        } catch (ExpiredJwtException ex) {
            return true;
        }
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
