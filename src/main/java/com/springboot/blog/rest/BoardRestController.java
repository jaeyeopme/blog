package com.springboot.blog.rest;

import com.springboot.blog.domain.Board;
import com.springboot.blog.service.BoardService;
import com.springboot.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static com.springboot.blog.config.SecurityConfig.NULL_MESSAGE_FORMAT;

@RequestMapping(value = "/api/boards")
@RestController
public class BoardRestController {

    private final UserService userService;
    private final BoardService boardService;

    public BoardRestController(UserService userService, BoardService boardService) {
        this.userService = userService;
        this.boardService = boardService;
    }

    @PostMapping(value = "{userId}")
    public ResponseEntity<String> write(@PathVariable Long userId, @ModelAttribute Board board, @RequestPart(required = false) MultipartFile image) {
        boardService.write(userService.findById(userId), getValidBoard(board), image);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<String> edit(@PathVariable Long id, @ModelAttribute Board board, @RequestPart(required = false) MultipartFile image) {
        boardService.edit(id, getValidBoard(board), image);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Board getValidBoard(Board board) {
        if (board.getTitle().trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE_FORMAT, "Title"));

        return board;
    }

}
