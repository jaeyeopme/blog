package me.jaeyeop.blog.comment.application.port.in;

import me.jaeyeop.blog.comment.adapter.in.CreateCommentCommand;

public interface CommentCommandUseCase {

  void create(Long authorId, CreateCommentCommand command);

}
