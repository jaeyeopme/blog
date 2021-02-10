package com.springboot.blog.service;

import com.springboot.blog.domain.Comment;
import com.springboot.blog.domain.User;

public interface CommentService {

    void write(User user, Long boardId, Comment newComment);

    void edit(Long id, Comment newComment);

    void delete(Long id);

}
