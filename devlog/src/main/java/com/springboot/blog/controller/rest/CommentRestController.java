package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.CommentService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/boards/{boardId}/comments", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> write(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody Comment comment) {
        return commentService.write(user, boardId, comment);
    }

    @PutMapping(value = "/comments/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody Comment newComment) {
        return commentService.modify(id, newComment);
    }

    @DeleteMapping(value = "/comments/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return commentService.delete(id);
    }
}
