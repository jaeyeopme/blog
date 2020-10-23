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


    // http 헤더와 http 바디에 대해서 깊은 이해가 필요해
    @PostMapping("users")
    public ResponseEntity<ApiResponse> join(@RequestBody User user) {
        return userService.save(user);
    }

    @GetMapping("users/{email}")
    public ResponseEntity<ApiResponse> findByEmail(@PathVariable String email) {
        return userService.findByEmail(email);
    }

    @PutMapping("users")
    public ResponseEntity<ApiResponse> update(@RequestBody User user) {
        return userService.update(user);
    }

}
