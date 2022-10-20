package me.jaeyeop.blog.comment.application.port.in;

import me.jaeyeop.blog.comment.adapter.in.UpdateCommentCommand;
import me.jaeyeop.blog.comment.adapter.in.command.CreateCommentCommand;

public interface CommentCommandUseCase {

  void create(Long authorId, CreateCommentCommand command);

  void update(Long authorId, Long commentId, UpdateCommentCommand command);

}
