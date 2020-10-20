package com.springboot.blog.service;

import com.springboot.blog.controller.rest.ApiResponse;
import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Transactional(readOnly = true)
    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Transactional
    public ResponseEntity<ApiResponse> save(Board board, User user) {
        try {
            board.setUser(user);
            boardRepository.save(board);
        } catch (Exception e) {
            throw new IllegalArgumentException("server error");
        }

        HttpStatus created = HttpStatus.CREATED;
        ApiResponse success = new ApiResponse(created, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, created);
    }

    @Transactional(readOnly = true)
    public Board findById(Long boardsId) {
        Board board = boardRepository.findById(boardsId).orElseThrow(() -> new IllegalArgumentException("not found board"));
        return board;
    }

    @Transactional
    public ResponseEntity<ApiResponse> update(Board board) {
        Board found_board = boardRepository.findById(board.getId()).orElseThrow(() -> new IllegalArgumentException("not found board"));
        found_board.setTitle(board.getTitle());
        found_board.setContent(board.getContent());

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

    @Transactional
    public ResponseEntity<ApiResponse> deleteById(Long boardId) {
        try {
            boardRepository.deleteById(boardId);
        } catch (Exception e) {
            throw new RuntimeException("not found board");
        }

        HttpStatus ok = HttpStatus.OK;
        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());

        return new ResponseEntity<>(success, ok);
    }

}
