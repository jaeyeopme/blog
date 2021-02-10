package com.springboot.blog.security;

import com.springboot.blog.util.MailService;
import com.springboot.blog.util.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class AuthenticationServiceImpl {

    private static final String SIGNUP_LINK = "http://localhost:8080/api/users/join/confirm?token=%s";

    private static final String INVALID_TOKEN_MESSAGE = "Invalid token.";
    private static final Long EXPIRATION_TIME = 60L;

    private final RedisService redisService;
    private final MailService mailService;

    public AuthenticationServiceImpl(RedisService redisService, MailService mailService) {
        this.redisService = redisService;
        this.mailService = mailService;
    }

    public void join(String subject, String email) {
        String token = UUID.randomUUID().toString();

        mailService.send(email, subject, String.format(SIGNUP_LINK, token));
        redisService.setData(token, email, EXPIRATION_TIME);
    }

    public String joinConfirmation(String token) {
        return redisService.getData(token)
                .map(email -> {
                    redisService.deleteData(token);
                    return email;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_MESSAGE));
    }

}
