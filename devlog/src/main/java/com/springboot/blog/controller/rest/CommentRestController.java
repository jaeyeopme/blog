package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

//@RestController
//public class CommentRestController {
//
//    private final CommentService commentService;
//
//    public CommentRestController(CommentService commentService) {
//        this.commentService = commentService;
//    }
//
//    @PostMapping("comments/{boardId}")
//    public ResponseEntity<ApiResponse> save(@PathVariable Long boardId, @AuthenticationPrincipal User user, @RequestBody Comment comment) {
//        return commentService.save(boardId, user, comment);
//    }
//
//    @PutMapping("comments")
//    public ResponseEntity<ApiResponse> update(@RequestBody Comment comment) {
//        return commentService.update(comment);
//    }
//
//    @DeleteMapping("comments/{id}")
//    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
//        return commentService.deleteById(id);
//    }
//}
