package com.edwinkesuma.springedmastore.infrastructure.security.jwt;

import com.edwinkesuma.springedmastore.features.user.domain.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    private final SecretKey secretKey;

    private final long jwtExpiration;

    public JwtUtil(
            @Value("${jwt.secret}") String jwtSecret,
            @Value("${jwt.expiration}") long jwtExpiration
    ) {

        this.secretKey =
                Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        this.jwtExpiration = jwtExpiration;
    }

    public String generateJwtToken(Authentication authentication) {

        User fetchedUser = (User) authentication.getPrincipal();

        String
                roles =
                authentication.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.joining(","));

        Date now = new Date();

        Date expiredAt = new Date(now.getTime() + jwtExpiration);

        return Jwts.builder()
                .issuer("edma-shop")
                .subject(fetchedUser.getEmail())
                .claim("userId", fetchedUser.getId())
                .claim("userName", fetchedUser.getUsername())
                .claim("email", fetchedUser.getEmail())
                .claim("role", roles)
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(secretKey)
                .compact();
    }
}