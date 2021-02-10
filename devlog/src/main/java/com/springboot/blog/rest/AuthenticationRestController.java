package com.springboot.blog.rest;

import com.springboot.blog.security.AuthenticationServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
public class AuthenticationRestController {

    private static final String EMAIL = "email";
    private static final String EMAIL_SEND_SUCCESS_MESSAGE = "Email send success.";
    private static final String JOIN_SUCCESS_MESSAGE = "Registration success.";
    private static final String EMAIL_NULL_MESSAGE = "Email cannot be <strong>null</strong>.";
    private static final String JOIN_SUBJECT = "Sign up Box";
    private static final String LOGIN_SUBJECT = "Sign in to Box";

    private final AuthenticationServiceImpl authenticationServiceImpl;

    public AuthenticationRestController(AuthenticationServiceImpl authenticationServiceImpl) {
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    @PostMapping("/join")
    public ResponseEntity<String> join(@RequestBody Map<String, String> request) {
        authenticationServiceImpl.join(JOIN_SUBJECT, nullValidate(request.get(EMAIL)));
        return ResponseEntity.status(HttpStatus.OK).body(EMAIL_SEND_SUCCESS_MESSAGE);
    }

    @GetMapping("/join/confirm")
    public ResponseEntity<String> joinConfirm(@RequestParam String token) {
        authenticationServiceImpl.joinConfirmation(token);
        return ResponseEntity.status(HttpStatus.CREATED).body(JOIN_SUCCESS_MESSAGE);
    }

    private String nullValidate(String email) {
        if (email.replaceAll(" ", "").isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, EMAIL_NULL_MESSAGE);

        return email;
    }

//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody Map<String, String> request) {
//        userService.login(request.get(EMAIL));
//        return ResponseEntity.status(HttpStatus.OK).body(EMAIL_SEND_SUCCESS_MESSAGE);
//    }
//
//    @GetMapping("/signin/confirm")
//    public ResponseEntity<String> loginConfirm(@RequestParam String token) {
//        userService.loginConfirmToken(token);
//        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(REGISTRATION_SUCCESS_MESSAGE);
//    }

}
