package me.jaeyeop.blog.comment.application.port.out;

import me.jaeyeop.blog.comment.domain.Comment;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
public interface CommentCommandPort {

  Comment save(Comment comment);

  void delete(Comment comment);

}
