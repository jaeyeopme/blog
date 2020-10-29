package com.springboot.blog.service;

import com.springboot.blog.entity.Board;
import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

//    @Transactional
//    public ResponseEntity<ApiResponse> save(Long boardId, User user, Comment comment) {
//        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException(String.format("not fount board - %d", boardId)));
//
//        try {
//            comment.setUser(user);
//            comment.setBoard(board);
//            commentRepository.save(comment);
//        } catch (Exception e) {
//            throw new RuntimeException("server error");
//        }
//
//        HttpStatus created = HttpStatus.CREATED;
//        ApiResponse success = new ApiResponse(created, "success", System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, created);
//    }
//
//    @Transactional
//    public ResponseEntity<ApiResponse> update(Comment comment) {
//        Comment found_comment = commentRepository.findById(comment.getId()).orElseThrow(() -> new IllegalArgumentException(String.format("not found comment - %d", comment.getId())));
//        found_comment.setContent(comment.getContent());
//
//        HttpStatus ok = HttpStatus.OK;
//        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, ok);
//    }
//
//    @Transactional
//    public ResponseEntity<ApiResponse> deleteById(Long id) {
//        try {
//            commentRepository.deleteById(id);
//        } catch (Exception e) {
//            new IllegalArgumentException(String.format("not found reply - %d", id));
//        }
//
//        HttpStatus ok = HttpStatus.OK;
//        ApiResponse success = new ApiResponse(ok, "success", System.currentTimeMillis());
//
//        return new ResponseEntity<>(success, ok);
//    }
}
