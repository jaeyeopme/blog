package com.springboot.blog.service;

import com.springboot.blog.entity.User;
import com.springboot.blog.entity.Comment;

public interface CommentService {

    void write(User user, Long boardId, Comment newComment);

    void edit(Long id, Comment newComment);

    void delete(Long id);

}
