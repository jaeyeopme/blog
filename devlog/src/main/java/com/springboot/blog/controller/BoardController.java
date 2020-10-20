package com.springboot.blog.controller;

import com.springboot.blog.entity.Board;
import com.springboot.blog.service.BoardService;
import com.springboot.blog.service.ReplyService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class BoardController {

    private final BoardService boardService;
    private final ReplyService replyService;

    public BoardController(BoardService boardService, ReplyService replyService) {
        this.boardService = boardService;
        this.replyService = replyService;
    }

    @GetMapping("/")
    public String index(Model model, @PageableDefault(size = 9, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("boards", boardService.findAll(pageable));
        return "index";
    }

    @GetMapping("write")
    public String writeForm() {
        return "board/board-write-form";
    }

    @GetMapping("modify/{boardId}")
    public String modifyForm(Model model, @PathVariable Long boardId) {
        Board board = boardService.findById(boardId);
        model.addAttribute("board", board);
        return "board/board-modify-form";
    }

    @GetMapping("detail/{boardId}")
    public String boardsDetailForm(Model model, @PathVariable Long boardId) {
        model.addAttribute("board", boardService.findById(boardId));
        return "board/board-detail-form";
    }
}
