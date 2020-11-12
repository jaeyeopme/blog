package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.UserService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PutMapping(value = "/users/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody User user) {
        return userService.modify(id, user);
    }

    @DeleteMapping(value = "/users/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return userService.deleteById(id);
    }

    @PutMapping(value = "/users/photo/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestPart MultipartFile photo) {
        return userService.modifyUserPhoto(id, photo);
    }

    @DeleteMapping(value = "/users/photo/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> modify(@PathVariable Long id) {
        return userService.deleteUserPhoto(id);
    }

}
