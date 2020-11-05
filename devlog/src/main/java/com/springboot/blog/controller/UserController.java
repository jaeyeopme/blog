package com.springboot.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("login")
    public String loginForm() {
        return "user/user-login-form";
    }

}
