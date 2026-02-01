package me.jaeyeop.blog.comment.application.port.out;

import java.util.List;

import me.jaeyeop.blog.comment.domain.Comment;

public interface CommentCommandPort {
    Comment save(Comment comment);

    void delete(Comment comment);

    void deleteAll(List<Comment> comments);
}
