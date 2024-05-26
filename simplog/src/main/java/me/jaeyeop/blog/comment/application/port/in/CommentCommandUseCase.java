package me.jaeyeop.blog.comment.application.port.in;

/**
 * @author jaeyeopme Created on 10/18/2022.
 */
public interface CommentCommandUseCase {

  Long write(WriteCommand command);

  void edit(EditCommand command);

  void delete(DeleteCommand command);

  record WriteCommand(
      Long authorId,
      Long targetId,
      String content
  ) {

  }

  record EditCommand(
      Long authorId,
      Long targetId,
      String newContent
  ) {

  }

  record DeleteCommand(
      Long authorId,
      Long targetId
  ) {

  }

}
