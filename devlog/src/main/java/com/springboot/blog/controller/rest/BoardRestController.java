package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("boards")
    public ResponseEntity<ApiResponse> save(@ModelAttribute Board board, @RequestPart(required = false) MultipartFile file, @AuthenticationPrincipal User user) {
        return boardService.save(board, file, user);
    }

    @PutMapping("boards")
    public ResponseEntity<ApiResponse> update(@ModelAttribute Board board, @RequestPart(required = false) MultipartFile file) {
        return boardService.update(board, file);
    }

    @DeleteMapping("boards/{id}")
    public ResponseEntity<ApiResponse> deleteById(@PathVariable Long id) {
        return boardService.deleteById(id);
    }

}
