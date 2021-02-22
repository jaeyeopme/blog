package com.springboot.blog.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import static com.springboot.blog.config.SecurityConfig.*;

@Slf4j
@Service
public class JwtService {

    private static final String AUTHORITIES_CLAIM = "authorities";
    private static final String ISSUER = "Box";
    private static final String ACCESS_TOKEN_SUBJECT = "ACCESS";
    private static final String REFRESH_TOKEN_SUBJECT = "REFRESH";
    private final String secret;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    public String generateAccessToken(String email, Collection<? extends GrantedAuthority> authorities) {
        return JWT_TOKEN_PREFIX + JWT.create()
                .withIssuer(ISSUER)
                .withAudience(email)
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withClaim(AUTHORITIES_CLAIM, authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_60_MINUTES))
                .sign(Algorithm.HMAC256(secret));
    }

    public String generateRefreshToken(String email) {
        return JWT_TOKEN_PREFIX + JWT.create()
                .withIssuer(ISSUER)
                .withAudience(email)
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_1_DAYS))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getAudience(DecodedJWT decodedToken) {
        return decodedToken.getAudience()
                .stream()
                .findFirst()
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_MESSAGE));
    }

    public Collection<? extends GrantedAuthority> getAuthorities(DecodedJWT decodedToken) {
        return decodedToken
                .getClaim(AUTHORITIES_CLAIM)
                .asList(SimpleGrantedAuthority.class);
    }

    public DecodedJWT getDecodedToken(String token) {
        try {
            return JWT.require(Algorithm.HMAC256(secret))
                    .build()
                    .verify(token.replace(JWT_TOKEN_PREFIX, ""));
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e.getCause());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_MESSAGE, e);
        }
    }

}
