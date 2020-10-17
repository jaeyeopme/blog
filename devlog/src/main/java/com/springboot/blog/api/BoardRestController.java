package com.springboot.blog.api;

import com.springboot.blog.config.auth.PrincipalDetail;
import com.springboot.blog.config.auth.PrincipalDetailService;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.service.BoardService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardRestController {

    private final BoardService boardService;

    public BoardRestController(BoardService boardService) {
        this.boardService = boardService;
    }

    @PostMapping("boards")
    public ResponseEntity<ApiResponse> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principalDetail) {
        return boardService.save(board, principalDetail.getUser());
    }

}
