package com.springboot.blog.service;

import com.springboot.blog.domain.Comment;
import com.springboot.blog.domain.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentServiceImpl implements CommentService {

    private final String NOT_FOUND_MESSAGE = "Not found %s.";
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    /**
     * 댓글 작성
     *
     * @param user
     * @param boardId
     * @param newComment
     */
    @Transactional
    @Override
    public void write(User user, Long boardId, Comment newComment) {
        boardRepository.findById(boardId)
                .map(findBoard -> {
                    newComment.setUser(user);
                    newComment.setBoard(findBoard);

                    return commentRepository.save(newComment);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, "board")));
    }

    /**
     * 댓글 수정
     *
     * @param id
     * @param newComment
     */
    @Transactional
    @Override
    public void edit(Long id, Comment newComment) {
        commentRepository.findById(id)
                .map(findComment -> {
                    findComment.setContent(newComment.getContent());

                    return findComment;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, "comment")));
    }

    /**
     * 댓글 삭제
     *
     * @param id
     */
    @Transactional
    @Override
    public void delete(Long id) {
        commentRepository.findById(id)
                .map(findComment -> {
                    commentRepository.delete(findComment);

                    return findComment;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format(NOT_FOUND_MESSAGE, "comment")));
    }

}
