package me.jaeyeop.blog.comment.application.port.in;

import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Create;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Delete;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Update;
import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 10/18/2022.
 */
public interface CommentCommandUseCase {

  void create(User author, Create request);

  void update(User author, Long commentId, Update request);

  void delete(User author, Delete request);

}
