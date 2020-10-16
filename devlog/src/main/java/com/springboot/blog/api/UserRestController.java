package com.springboot.blog.api;

import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/api")
@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users")
    public ResponseEntity<ApiResponse> join(@RequestBody User user) {
        user.setRole(UserRole.ROLE_USER);
        return userService.join(user);
    }

    @GetMapping("users/{email}")
    public ResponseEntity<ApiResponse> validationEmail(@PathVariable String email) {
        return userService.validationEmail(email.trim());
    }

}
