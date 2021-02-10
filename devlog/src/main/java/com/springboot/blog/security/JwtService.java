package com.springboot.blog.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private static final String ISSUER = "Box";
    private static final String ACCESS = "ACCESS";
    private static final String REFRESH = "REFRESH";

    private static final int ACCESS_TOKEN_EXPIRATION_TIME = 60000 * 5;
    private static final int REFRESH_TOKEN_EXPIRATION_TIME = 60000 * 1440;

    private final String secret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateAccessToken(String email) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withAudience(email)
                .withSubject(ACCESS)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withIssuer(ISSUER)
                .withAudience(email)
                .withSubject(REFRESH)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(secret));
    }

    public void validateToken(String token) {
        JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token);
    }

    public String getAccessToken(HttpServletRequest request) {
        return request.getHeader("Authorization").replace("Bearer ", "");
    }

    public String getAudience(String token) {
        return JWT.require(Algorithm.HMAC256(secret))
                .build()
                .verify(token)
                .getAudience()
                .stream()
                .findFirst()
                .get();
    }

}
