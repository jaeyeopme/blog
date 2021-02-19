package com.springboot.blog.rest;

import com.springboot.blog.domain.Comment;
import com.springboot.blog.service.BoardService;
import com.springboot.blog.service.CommentService;
import com.springboot.blog.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import static com.springboot.blog.config.SecurityConfig.NULL_MESSAGE_FORMAT;

@RestController(value = "/api/comments")
public class CommentRestController {

    private final CommentService commentService;
    private final UserService userService;
    private final BoardService boardService;

    public CommentRestController(CommentService commentService, UserService userService, BoardService boardService) {
        this.commentService = commentService;
        this.userService = userService;
        this.boardService = boardService;
    }

    @PostMapping(value = "{boardId}")
    public ResponseEntity<String> write(@AuthenticationPrincipal Authentication authentication, @PathVariable Long boardId, @RequestBody Comment comment) {
        commentService.write(userService.findByEmail((String) authentication.getPrincipal()), boardService.findById(boardId), getValidComment(comment));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping(value = "{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody Comment comment) {
        commentService.edit(id, getValidComment(comment));
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping(value = "{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private Comment getValidComment(Comment comment) {
        if (comment.getContent().trim().isEmpty())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format(NULL_MESSAGE_FORMAT, "Comment"));

        return comment;
    }

}
