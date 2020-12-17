package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/join")
    public String joinForm() {
        return "join";
    }

    @GetMapping("/setting")
    public String settingForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "setting";
    }
}
