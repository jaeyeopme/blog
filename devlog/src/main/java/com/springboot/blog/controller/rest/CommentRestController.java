package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController(value = "/api")
public class CommentRestController {

    private final CommentServiceImpl commentServiceImpl;

    @Autowired
    public CommentRestController(CommentServiceImpl commentServiceImpl) {
        this.commentServiceImpl = commentServiceImpl;
    }

    @PostMapping(value = "/boards/{boardId}/comments")
    public ResponseEntity<String> write(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody Comment newComment) {
        commentServiceImpl.write(user, boardId, newComment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "/comments/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody Comment newComment) {
        commentServiceImpl.edit(id, newComment);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "/comments/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commentServiceImpl.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
