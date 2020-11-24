package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.BoardService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(value = BoardRestController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class BoardRestController {

    public static final String BASE_URL = "/api/boards";
    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<?> write(@AuthenticationPrincipal User newUser, @RequestBody Board newBoard, @RequestPart(required = false) MultipartFile newPhoto) {
        System.out.println("Asdfasd");
        Board board = boardService.write(newUser, newBoard, newPhoto);

        Link selfRel = linkTo(this.getClass())
                .slash(board.getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

//    @PutMapping(value = "/{id}")
//    public ResponseEntity<String> modify(@PathVariable Long id, @ModelAttribute Board board, @RequestPart(required = false) MultipartFile thumbnail) {
//        return boardService.modify(id, board, thumbnail);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public ResponseEntity<String> deleteById(@PathVariable Long id) {
//        return boardService.deleteById(id);
//    }

}
