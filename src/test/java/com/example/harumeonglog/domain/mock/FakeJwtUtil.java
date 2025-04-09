package com.example.harumeonglog.domain.mock;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;

public class FakeJwtUtil {

    private static final Duration expiration = Duration.ofMillis(100000000L);

    public static String createToken(UserDetails userDetails, String secret) {
        Instant now = Instant.now();
        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("authorization", userDetails.getAuthorities())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(expiration)))
                .signWith(secretKey)
                .compact();
    }
}
