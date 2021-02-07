package com.springboot.blog.controller;

import com.springboot.blog.security.Principal;
import com.springboot.blog.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/setting")
    public String settingForm(@AuthenticationPrincipal Principal principal, Model model) {
        model.addAttribute("user", principal.getUser());

        return "user/setting";
    }

}
