package com.springboot.blog.service;

import com.springboot.blog.domain.Board;
import com.springboot.blog.domain.Comment;
import com.springboot.blog.domain.User;

public interface CommentService {

    void write(User user, Board board, Comment comment);

    void edit(Long id, Comment comment);

    void delete(Long id);

}
