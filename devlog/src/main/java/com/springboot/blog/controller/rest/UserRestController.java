package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    public ResponseEntity<ApiResponse> join(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("users/{email}")
    public ResponseEntity<ApiResponse> validationEmail(@PathVariable String email) {
        return userService.validationEmail(email);
    }

}
