package com.springboot.blog.rest;

import com.springboot.blog.domain.User;
import com.springboot.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RequestMapping(value = "/api/users")
@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

//    @PostMapping("/join")
//    public ResponseEntity<String> signup(@RequestBody Map<String, String> request) {
//        userService.signUp(request.get(EMAIL));
//        return ResponseEntity.status(HttpStatus.OK).body(EMAIL_SEND_SUCCESS_MESSAGE);
//    }
//
//    @GetMapping("/join/confirm")
//    public ResponseEntity<String> signUpConfirm(@RequestParam String token) {
//        userService.signUpConfirm(token);
//        return ResponseEntity.status(HttpStatus.CREATED).body(REGISTRATION_SUCCESS_MESSAGE);
//    }
//
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

//    private User formValidator(User user) {
//        if (user.getEmail().replaceAll(" ", "").isEmpty())
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE, "Email"));
//
//        return user;
//    }

}
