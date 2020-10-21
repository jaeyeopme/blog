package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Reply;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReplyRestController {

    private final ReplyService replyService;

    public ReplyRestController(ReplyService replyService) {
        this.replyService = replyService;
    }

    @PostMapping("replies/{boardId}")
    public ResponseEntity<ApiResponse> save(@PathVariable Long boardId, @AuthenticationPrincipal User user, @RequestBody Reply reply) {
        return replyService.save(boardId, user, reply);
    }

    @PutMapping("replies")
    public ResponseEntity<ApiResponse> update(@RequestBody Reply reply) {
        return replyService.update(reply);
    }

    @DeleteMapping("replies/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Long id) {
        return replyService.deleteById(id);
    }
}
