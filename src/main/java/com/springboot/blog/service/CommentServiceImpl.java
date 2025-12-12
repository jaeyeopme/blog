package com.springboot.blog.service;

import com.springboot.blog.domain.Board;
import com.springboot.blog.domain.Comment;
import com.springboot.blog.domain.User;
import com.springboot.blog.repository.CommentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import static com.springboot.blog.config.SecurityConfig.NOT_FOUND_MESSAGE_FORMAT;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Transactional
    @Override
    public void write(User user, Board board, Comment comment) {
        comment.setUser(user);
        comment.setBoard(board);
        commentRepository.save(comment);
    }

    @Transactional
    @Override
    public void edit(Long id, Comment newComment) {
        commentRepository.findById(id)
                .map(findComment -> {
                    findComment.setContent(newComment.getContent());

                    return findComment;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE_FORMAT, "comment")));
    }

    @Transactional
    @Override
    public void delete(Long id) {
        commentRepository.findById(id)
                .map(findComment -> {
                    commentRepository.delete(findComment);

                    return findComment;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE_FORMAT, "comment")));
    }

}
