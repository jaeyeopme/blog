package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.BoardService;
import org.springframework.hateoas.MediaTypes;
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

    @PostMapping(value = "/boards", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> write(@ModelAttribute Board board, @RequestPart(required = false) MultipartFile thumbnail, @AuthenticationPrincipal User user) {
        return boardService.write(board, thumbnail, user);
    }

    @PutMapping(value = "/boards/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> modify(@PathVariable Long id, @ModelAttribute Board board, @RequestPart(required = false) MultipartFile thumbnail) {
        return boardService.modify(id, board, thumbnail);
    }

    @DeleteMapping(value = "/boards/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        return boardService.deleteById(id);
    }

}
