package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequestMapping(value = "/api/users")
@RestController
public class UserRestController {

    public static final String EMAIL = "email";
    public static final String EMAIL_SEND_SUCCESS_MESSAGE = "Email send success.";
    public static final String REGISTRATION_SUCCESS_MESSAGE = "Registration success.";
    private final String NULL_MESSAGE = "%s cannot be <strong>null</strong>.";

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registration(@RequestBody Map<String, String> request) {
        userService.registration(request.get(EMAIL));
        return ResponseEntity.status(HttpStatus.OK).body(EMAIL_SEND_SUCCESS_MESSAGE);
    }

    @GetMapping("/register/confirm")
    public ResponseEntity<String> registrationConfirm(@RequestParam String token) {
        userService.registrationConfirmToken(token);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(REGISTRATION_SUCCESS_MESSAGE);
    }

    private User formValidator(User user) {
        if (user.getEmail().replaceAll(" ", "").isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE, "Email"));

        return user;
    }

}
