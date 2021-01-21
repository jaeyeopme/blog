package com.springboot.blog.service;

import com.springboot.blog.entity.Comment;
import com.springboot.blog.entity.User;
import com.springboot.blog.repository.BoardRepository;
import com.springboot.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    @Autowired
    public CommentService(CommentRepository commentRepository, BoardRepository boardRepository) {
        this.commentRepository = commentRepository;
        this.boardRepository = boardRepository;
    }

    /**
     * 댓글 작성
     *
     * @param user
     * @param boardId
     * @param newComment
     * @return
     */
    @Transactional
    public Comment write(User user, Long boardId, Comment newComment) {
        return boardRepository.findById(boardId)
                .map(findBoard -> {
                    newComment.setUser(user);
                    newComment.setBoard(findBoard);

                    return commentRepository.save(newComment);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found board."));
    }

    /**
     * 댓글 수정
     *
     * @param id
     * @param newComment
     * @return
     */
    @Transactional
    public Comment edit(Long id, Comment newComment) {
        return commentRepository.findById(id)
                .map(findComment -> {
                    findComment.setContent(newComment.getContent());

                    return findComment;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found comment."));
    }

    /**
     * 댓글 삭제
     *
     * @param id
     */
    @Transactional
    public void delete(Long id) {
        commentRepository.findById(id)
                .map(findComment -> {
                    commentRepository.delete(findComment);

                    return findComment;
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found comment."));
    }

}
