package com.springboot.blog.rest;

import com.springboot.blog.domain.User;
import com.springboot.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping(value = "/api/users")
@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PutMapping("{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @ModelAttribute User user, @RequestPart(required = false) MultipartFile picture) {
        userService.modify(id, user, picture);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
