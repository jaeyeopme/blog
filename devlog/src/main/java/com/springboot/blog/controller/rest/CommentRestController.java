package com.springboot.blog.controller.rest;

import com.springboot.blog.controller.BoardController;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RequestMapping(produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class CommentRestController {

    private final CommentService commentService;

    @Autowired
    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/boards/{boardId}/comments")
    public ResponseEntity<String> write(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody Comment newComment) {
        commentService.write(user, boardId, newComment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/comments/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody Comment newComment) {
        commentService.edit(id, newComment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/comments/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
