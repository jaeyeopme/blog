package com.springboot.blog.controller.rest;

import com.springboot.blog.entity.Reply;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.ReplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

}
