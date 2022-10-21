package me.jaeyeop.blog.comment.application.port.out;

import me.jaeyeop.blog.comment.domain.Comment;

public interface CommentCommandPort {

  void delete(Comment comment);

}
