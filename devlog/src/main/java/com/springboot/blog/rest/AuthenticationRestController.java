package com.springboot.blog.rest;

import com.springboot.blog.security.AuthenticationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

import static com.springboot.blog.config.SecurityConfig.*;

@RestController
public class AuthenticationRestController {

    private final AuthenticationService authenticationService;

    public AuthenticationRestController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> sendSignupToken(@RequestBody Map<String, String> request) {
        String sendSuccessMessage = authenticationService.sendSignupToken(getValidEmail(request.get(EMAIL_REQUEST)));

        return ResponseEntity.status(HttpStatus.OK).body(sendSuccessMessage);
    }

    @GetMapping("/signup/confirm")
    public ResponseEntity<String> signup(@RequestParam String token) {
        authenticationService.signup(token);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> sendLoginToken(@RequestBody Map<String, String> request) {
        String sendSuccessMessage = authenticationService.sendLoginToken(getValidEmail(request.get(EMAIL_REQUEST)));

        return ResponseEntity.status(HttpStatus.OK).body(sendSuccessMessage);
    }

    @GetMapping("/login/confirm")
    public ResponseEntity<String> login(@RequestParam String token) {
        String accessToken = authenticationService.login(token);
        return ResponseEntity.status(HttpStatus.OK).header(AUTHORIZATION_HEADER, accessToken).build();
    }

    private String getValidEmail(String email) {
        if (email.trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE_FORMAT, "Email"));

        return email;
    }

}
