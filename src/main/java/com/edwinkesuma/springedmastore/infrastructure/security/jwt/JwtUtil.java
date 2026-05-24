package com.edwinkesuma.springedmastore.infrastructure.security.jwt;

import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {

    private final SecretKey secretKey;

    private final long jwtAccessExpiration;
    private final long jwtRefreshExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.access-expiration}") long jwtAccessExpiration,
            @Value("${jwt.refresh-expiration}") long jwtRefreshExpiration
    ) {

        this.secretKey =
                Keys.hmacShaKeyFor(
                        jwtSecret.getBytes(StandardCharsets.UTF_8)
                );

        this.jwtAccessExpiration = jwtAccessExpiration;
        this.jwtRefreshExpiration = jwtRefreshExpiration;
    }

    public String generateAccessToken(User user, List<String> roles) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("userId", user.getId());
        claims.put("userName", user.getUsername());
        claims.put("roles", roles);
        claims.put("type", "access");

        return buildToken(
                claims,
                user.getEmail(),
                jwtAccessExpiration
        );
    }

    public String generateRefreshToken(User user) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("type", "refresh");

        return buildToken(
                claims,
                user.getEmail(),
                jwtRefreshExpiration
        );
    }

    private String buildToken(
            Map<String, Object> claims,
            String subject,
            long expiration
    ) {

        Date now = new Date();

        Date expiredAt =
                new Date(now.getTime() + expiration);

        return Jwts.builder()
                .issuer("edma-shop")
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public String extractTokenType(String token) {
        return extractAllClaims(token)
                .get("type", String.class);
    }

    public boolean isAccessToken(String token) {
        return extractTokenType(token)
                .equals("access");
    }

    public boolean isRefreshToken(String token) {
        return extractTokenType(token)
                .equals("refresh");
    }

    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {

            return false;
        }
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}