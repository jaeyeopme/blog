package me.jaeyeop.blog.comment.application.port.in;

import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Create;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Delete;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Update;

public interface CommentCommandUseCase {

  void create(Long authorId, Create request);

  void update(Long authorId, Long commentId, Update request);

  void delete(Long authorId, Delete request);

}
