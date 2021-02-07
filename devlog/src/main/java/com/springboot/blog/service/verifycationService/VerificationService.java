package com.springboot.blog.service.verifycationService;

import com.springboot.blog.util.MailService;
import com.springboot.blog.util.RedisService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class VerificationService {

    private static final String REGISTRATION_LINK = "http://localhost:8080/api/users/register/confirm?token=%s";

    private static final String REGISTER_SUBJECT = "Sign up Box";
    private static final String LOGIN_SUBJECT = "Sign in to Box";

    private static final String INVALID_TOKEN_MESSAGE = "Token not valid.";
    private static final Long EXPIRED_TIME = 60L;
    private static final String FROM = "Box";

    private final RedisService redisService;
    private final MailService mailService;


    public VerificationService(RedisService redisService, MailService mailService) {
        this.redisService = redisService;
        this.mailService = mailService;
    }

    public void register(String email) {
        String token = UUID.randomUUID().toString();

        mailService.send(FROM, email, REGISTER_SUBJECT, String.format(REGISTRATION_LINK, token));
        redisService.setDataExpire(token, email, EXPIRED_TIME);
    }

    public void login(String email) {
        String token = UUID.randomUUID().toString();

        mailService.send(FROM, email, LOGIN_SUBJECT, String.format(REGISTRATION_LINK, token));
        redisService.setDataExpire(token, email, EXPIRED_TIME);
    }

    public String confirmToken(String token) {
        String email;

        try {
            email = redisService.getData(token);
            redisService.deleteData(token);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, INVALID_TOKEN_MESSAGE);
        }

        return email;
    }

}
