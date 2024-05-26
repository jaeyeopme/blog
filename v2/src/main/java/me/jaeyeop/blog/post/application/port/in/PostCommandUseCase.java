package me.jaeyeop.blog.post.application.port.in;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
public interface PostCommandUseCase {

  Long write(WriteCommand command);

  void edit(EditCommand command);

  void delete(DeleteCommand command);

  record WriteCommand(Long authorId, String title, String content) {

  }

  record EditCommand(Long authorId, Long targetId, String newTitle, String newContent) {

  }

  record DeleteCommand(Long authorId, Long targetId) {

  }

}
