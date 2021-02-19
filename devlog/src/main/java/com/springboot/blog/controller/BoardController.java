package com.springboot.blog.controller;

import com.springboot.blog.service.BoardServiceImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    private final BoardServiceImpl boardServiceImpl;

    public BoardController(BoardServiceImpl boardServiceImpl) {
        this.boardServiceImpl = boardServiceImpl;
    }

    @GetMapping("/")
    public String index(Model model, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boards", boardServiceImpl.findAll(pageable));
        return "index";
    }

    @GetMapping("/board/{id}")
    public String boardForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardServiceImpl.findById(id));
        return "board/board";
    }

    @GetMapping("/write")
    public String writeForm() {
        return "board/write";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardServiceImpl.findById(id));
        return "board/edit";
    }

}
