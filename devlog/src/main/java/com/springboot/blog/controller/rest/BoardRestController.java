package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("boards")
    public ResponseEntity<ApiResponse> save(@RequestBody Board board, @AuthenticationPrincipal User user) {
        return boardService.save(board, user);
    }

    @PutMapping("boards")
    public ResponseEntity<ApiResponse> update(@RequestBody Board board) {
        return boardService.update(board);
    }

    @DeleteMapping("boards/{boardsId}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long boardsId) {
        return boardService.deleteById(boardsId);
    }

}
