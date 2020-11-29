package com.springboot.blog.controller.rest;

import com.springboot.blog.controller.BoardController;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class BoardRestController {


    private final BoardService boardService;

    @Autowired
    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping(value = "/boards")
    public ResponseEntity<String> write(@AuthenticationPrincipal User user, @ModelAttribute Board newBoard, @RequestPart(required = false) MultipartFile newPhoto) {
        Board board = boardService.write(user, formValidation(newBoard), newPhoto);

        Link selfRel = linkTo("/boards")
                .slash(board.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).body(String.format("{id: %d}", board.getId()));
    }

    @PutMapping(value = "/boards/{id}")
    public ResponseEntity<String> edit(@PathVariable Long id, @ModelAttribute Board newBoard, @RequestPart(required = false) MultipartFile newPhoto) {
        Board board = boardService.edit(id, newBoard, newPhoto);

        Link selfRel = linkTo("/boards")
                .slash(board.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

    @DeleteMapping(value = "/boards/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boardService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * 입력 값 유효성 검사
     *
     * @param newBoard
     * @return
     */
    private Board formValidation(Board newBoard) {
        if (newBoard.getTitle().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Title cannot be null.");
        }
        return newBoard;
    }

}
