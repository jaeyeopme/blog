package com.springboot.blog.controller;

import com.springboot.blog.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.springboot.blog.config.SecurityConfig.AUTHORIZATION_HEADER;

@Controller
public class BoardController {

    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/")
    public String index(HttpServletRequest request, HttpServletResponse response, Model model, @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        String b = request.getHeader(AUTHORIZATION_HEADER);
        System.out.println("response.hea = " + b);

        if (false) {
            System.out.println("header = " + response.getHeader(AUTHORIZATION_HEADER));
        }

        model.addAttribute("boards", boardService.findAll(pageable));
        return "index";
    }

    @GetMapping("/board/{id}")
    public String boardForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "board/board";
    }

    @GetMapping("/write")
    public String writeForm() {
        return "board/write";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("board", boardService.findById(id));
        return "board/edit";
    }

}
