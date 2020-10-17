package com.springboot.blog.controller;

import com.springboot.blog.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    private final UserRepository userRepository;

    public BoardController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("write")
    public String writeForm() {
        return "board/write-form";
    }
}
