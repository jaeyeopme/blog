package com.springboot.blog.controller.rest;

import com.springboot.blog.controller.BoardController;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.CommentService;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping(value = CommentRestController.BASE_URL, produces = MediaTypes.HAL_JSON_VALUE)
@RestController
public class CommentRestController {

    public static final String BASE_URL = "/boards/{boardId}/comments";
    private final CommentService commentService;

    public CommentRestController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * 댓글 작성
     * @param user
     * @param boardId
     * @param newComment
     * @return
     */
    @PostMapping
    public ResponseEntity<String> write(@AuthenticationPrincipal User user, @PathVariable Long boardId, @RequestBody Comment newComment) {
        Comment comment = commentService.write(user, boardId, newComment);

        Link selfRel = linkTo(BoardController.class)
                .slash(comment.getBoard().getId()).withSelfRel();

        return ResponseEntity.created(selfRel.toUri()).build();
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> modify(@PathVariable Long id, @RequestBody Comment newComment) {
        return commentService.modify(id, newComment);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return commentService.delete(id);
    }
}
