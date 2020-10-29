package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

//    @GetMapping("join")
//    public String joinForm() {
//        return "user/user-join-form";
//    }

    @GetMapping("login")
    public String loginForm() {
        return "user/user-login-form";
    }

    @GetMapping("settings")
    public String settingForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "user/user-settings-form";
    }

}
