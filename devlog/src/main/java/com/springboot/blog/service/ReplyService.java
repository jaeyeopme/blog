package com.springboot.blog.service;

import com.springboot.blog.controller.rest.ApiResponse;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.Reply;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReplyService {

    private final ReplyRepository replyRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public ReplyService(ReplyRepository replyRepository, BoardRepository boardRepository) {
        this.replyRepository = replyRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(Long boardId, User user, Reply reply) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException(String.format("not fount board - %d", boardId)));

        try {
            reply.setUser(user);
            reply.setBoard(board);
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
        Reply found_reply = replyRepository.findById(reply.getId()).orElseThrow(() -> new IllegalArgumentException(String.format("not found reply - %d", reply.getId())));
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
            new IllegalArgumentException(String.format("not found reply - %d", id));
        }

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }
}
