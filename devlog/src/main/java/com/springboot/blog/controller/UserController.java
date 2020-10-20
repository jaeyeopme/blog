package com.springboot.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping("join")
    public String joinForm() {
        return "user/join-form";
    }

    @GetMapping("login")
    public String loginForm() {
        return "user/login-form";
    }

}
