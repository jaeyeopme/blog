package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.CommentService;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentRestController {

    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping(value = "/boards/{boardId}/comments", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<String> write(@AuthenticationPrincipal User user, @PathVariable("boardId") Long boardId, @RequestBody Comment comment) {
        return commentService.write(user, boardId, comment);
    }
}
