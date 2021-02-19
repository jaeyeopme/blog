package com.springboot.blog.security;

import com.springboot.blog.domain.Role;
import com.springboot.blog.domain.User;
import com.springboot.blog.service.UserService;
import com.springboot.blog.util.MailService;
import com.springboot.blog.util.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

import static com.springboot.blog.config.SecurityConfig.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserService userService;
    private final RedisService redisService;
    private final JwtService jwtService;
    private final MailService mailService;
    private final String defaultPicture;

    public AuthenticationServiceImpl(UserService userService,
                                     RedisService redisService,
                                     MailService mailService,
                                     JwtService jwtService,
                                     @Value("${cloud.aws.s3.default-picture}") String defaultPicture) {
        this.userService = userService;
        this.redisService = redisService;
        this.mailService = mailService;
        this.jwtService = jwtService;
        this.defaultPicture = defaultPicture;
    }

    @Transactional(readOnly = true)
    @Override
    public String sendSignupToken(String validEmail) {
        if (userService.existsByEmail(validEmail))
            throw new ResponseStatusException(HttpStatus.CONFLICT, EXISTS_EMAIL_MESSAGE);

        String authenticationToken = UUID.randomUUID().toString();
        redisService.setToken(authenticationToken, validEmail, EXPIRATION_TIME_10_MINUTES);

        return mailService.send(validEmail, SIGNUP_SUBJECT, String.format(SIGNUP_LINK, authenticationToken));
    }

    @Transactional
    @Override
    public void signup(String token) {
        userService.save(
                User.builder()
                        .role(Role.ROLE_USER)
                        .email(redisService.getEmailFromToken(token))
                        .pictureUrl(defaultPicture)
                        .build());
    }

    @Transactional(readOnly = true)
    @Override
    public String sendLoginToken(String validEmail) {
        if (!userService.existsByEmail(validEmail))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(NOT_FOUND_MESSAGE_FORMAT, "email"));

        String authenticationToken = UUID.randomUUID().toString();
        redisService.setToken(authenticationToken, validEmail, EXPIRATION_TIME_10_MINUTES);

        return mailService.send(validEmail, LOGIN_SUBJECT, String.format(LOGIN_LINK, authenticationToken));
    }

    @Transactional(readOnly = true)
    @Override
    public String login(String token) {
        User user = userService.findByEmail(redisService.getEmailFromToken(token));

        return jwtService.generateAccessToken(user.getEmail(), user.getAuthorities());
    }

}
