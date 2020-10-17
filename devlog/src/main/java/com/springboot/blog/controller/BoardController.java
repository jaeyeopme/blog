package com.springboot.blog.controller;

import com.springboot.blog.entity.Board;
import com.springboot.blog.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Board> boards = boardService.findAll();
        model.addAttribute("boards", boards);
        return "index";
    }

    @GetMapping("write")
    public String writeForm() {
        return "board/write-form";
    }
}
