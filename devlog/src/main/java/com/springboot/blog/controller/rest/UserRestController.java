package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/users", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> signup(@RequestBody User user) {
        return userService.signup(user);
    }

}
