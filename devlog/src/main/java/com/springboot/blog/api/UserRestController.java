package com.springboot.blog.api;

import com.springboot.blog.entity.User;
import com.springboot.blog.entity.UserRole;
import com.springboot.blog.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api")
@RestController
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("users/join")
    public ResponseEntity<ApiResponse> join(@RequestBody User user) {
        user.setRole(UserRole.ROLE_USER);
        return userService.join(user);
    }

//    @PostMapping("users/login")
//    public ResponseEntity<ApiResponse> login(@RequestBody User user, HttpSession httpSession) {
//        return userService.login(user, httpSession);
//    }

    @GetMapping("users/{email}")
    public ResponseEntity<ApiResponse> validationEmail(@PathVariable String email) {
        return userService.validationEmail(email.trim());
    }

}
