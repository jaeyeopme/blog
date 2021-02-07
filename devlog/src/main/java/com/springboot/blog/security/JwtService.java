package com.springboot.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    public static final String ISSUER = "Box";
    public static final String ACCESS = "ACCESS";
    public static final String REFRESH = "REFRESH";
    private final String secret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = secret;
    }

    public String generateAccessToken(String email) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withAudience(email)
                .withSubject(ACCESS)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 5)))
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withAudience(email)
                .withSubject(REFRESH)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 1440)))
                .sign(Algorithm.HMAC256(secret));
    }

    public void validateToken(String audience, String token) {
        JWT.require(Algorithm.HMAC256(secret))
                .withAudience(audience)
                .build()
                .verify(token);
    }

}
