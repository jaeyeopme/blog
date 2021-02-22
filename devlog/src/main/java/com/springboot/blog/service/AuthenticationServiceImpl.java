package com.springboot.blog.service;

import com.springboot.blog.domain.Role;
import com.springboot.blog.domain.User;
import com.springboot.blog.jwt.JwtService;
import com.springboot.blog.util.MailService;
import com.springboot.blog.util.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public String sendSignupToken(String email) {
        if (userService.existsByEmail(email))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(EXISTS_EMAIL_MESSAGE_FORMAT, email));

        String authenticationToken = UUID.randomUUID().toString();
        redisService.setToken(authenticationToken, email, EXPIRATION_TIME_60_MINUTES);

        return mailService.send(email, SIGNUP_SUBJECT, String.format(SIGNUP_LINK, authenticationToken));
    }

    @Transactional
    @Override
    public void signup(String token) {
        String email = redisService.getEmailFromToken(token);

        User user = userService.save(User.builder()
                .role(Role.ROLE_USER)
                .email(email)
                .name(email.split("@")[0])
                .pictureUrl(defaultPicture)
                .build());

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities()));
    }

    @Transactional(readOnly = true)
    @Override
    public String sendLoginToken(String validEmail) {
        if (!userService.existsByEmail(validEmail))
            throw new ResponseStatusException(HttpStatus.CONFLICT, String.format(NOT_FOUND_MESSAGE_FORMAT, "email"));

        String authenticationToken = UUID.randomUUID().toString();
        redisService.setToken(authenticationToken, validEmail, EXPIRATION_TIME_60_MINUTES);

        return mailService.send(validEmail, LOGIN_SUBJECT, String.format(LOGIN_LINK, authenticationToken));
    }

    @Transactional(readOnly = true)
    @Override
    public String login(String token) {
        User user = userService.findByEmail(redisService.getEmailFromToken(token));

        SecurityContextHolder.getContext()
                .setAuthentication(new UsernamePasswordAuthenticationToken(user.getEmail(), null, user.getAuthorities()));

        return jwtService.generateAccessToken(user.getEmail(), user.getAuthorities());
    }

}
