package com.springboot.blog.service;

import com.springboot.blog.controller.rest.ApiResponse;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.Reply;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.repository.ReplyRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    public ReplyService(ReplyRepository replyRepository, BoardRepository boardRepository) {
        this.replyRepository = replyRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(Long boardId, User user, Reply reply) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("not fount board"));
        reply.setUser(user);
        reply.setBoard(board);

        try {
            replyRepository.save(reply);
        } catch (Exception e) {
            throw new RuntimeException("server error");
        }

        HttpStatus created = HttpStatus.CREATED;
        ApiResponse success = new ApiResponse(created, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, created);
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(Reply reply) {
        Reply found_reply = replyRepository.findById(reply.getId()).orElseThrow(() -> new IllegalArgumentException("not found reply"));
        found_reply.setContent(reply.getContent());

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

    @Transactional
    public ResponseEntity<ApiResponse> deleteById(Long id) {
        try {
            replyRepository.deleteById(id);
        } catch (Exception e) {
            new IllegalArgumentException("not found reply");
        }

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }
}
