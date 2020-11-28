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
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    @Transactional
    public Comment write(User user, Long boardId, Comment newComment) {
        return boardRepository.findById(boardId)
                .map(findBoard -> {
                    newComment.setUser(user);
                    newComment.setBoard(findBoard);
                    return commentRepository.save(newComment);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
    }

    @Transactional
    public ResponseEntity<String> modify(Long id, Comment newComment) {
        return commentRepository.findById(id)
                .map(comment -> {
                    comment.setContent(newComment.getContent());
                    return ResponseEntity.ok().body("{}");
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."));
    }

    @Transactional
    public ResponseEntity<String> delete(Long id) {
        commentRepository.deleteById(id);
        return ResponseEntity.ok("{}");
    }
}
