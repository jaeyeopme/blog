package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.User;
import com.springboot.blog.entity.Board;
import com.springboot.blog.service.BoardServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequestMapping(value = "/api/boards")
@RestController
public class BoardRestController {

    private final BoardServiceImpl boardServiceImpl;

    @Autowired
    public BoardRestController(BoardServiceImpl boardServiceImpl) {
        this.boardServiceImpl = boardServiceImpl;
    }

    @PostMapping
    public ResponseEntity<String> write(@AuthenticationPrincipal User user, @ModelAttribute Board newBoard, @RequestPart(required = false) MultipartFile newPhoto) {
        boardServiceImpl.write(user, formValidation(newBoard), newPhoto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> edit(@PathVariable Long id, @ModelAttribute Board newBoard, @RequestPart(required = false) MultipartFile newPhoto) {
        boardServiceImpl.edit(id, newBoard, newPhoto);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boardServiceImpl.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 입력 값 유효성 검사
     *
     * @param newBoard
     * @return
     */
    private Board formValidation(Board newBoard) {
        if (newBoard.getTitle().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null.");
        }
        return newBoard;
    }

}
