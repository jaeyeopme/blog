package com.springboot.blog.controller;

import com.springboot.blog.entity.User;
import com.springboot.blog.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/")
    public String index(Model model, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boards", boardService.findAll(pageable));
        return "index";
    }

    @GetMapping("boards/{id}")
    public String detailForm(@AuthenticationPrincipal User user, Model model, @PathVariable Long id) {
        model.addAttribute("user", user);
        model.addAttribute("board", boardService.findById(id));
        return "board/board-detail-form";
    }

    @GetMapping("write")
    public String writeForm(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("user", user);
        return "board/board-write-form";
    }

    @GetMapping("modify/{id}")
    public String modifyForm(@AuthenticationPrincipal User user, Model model, @PathVariable Long id) {
        model.addAttribute("user", user);
        model.addAttribute("board", boardService.findById(id));
        return "board/board-modify-form";
    }

}
