package com.springboot.blog.controller;

import com.springboot.blog.service.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

import static com.springboot.blog.config.SecurityConfig.AUTHORIZATION_HEADER;

@Controller
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping("/signup/confirm")
    public String signup(@RequestParam String token) {
        authenticationService.signup(token);
        return "redirect:/";
    }

    @GetMapping("/login/confirm")
    public String login(@RequestParam String token, HttpServletResponse response) {
//        String accessToken = authenticationService.login(token);
        response.setHeader(AUTHORIZATION_HEADER, token);
        return "redirect:/";
    }

}
