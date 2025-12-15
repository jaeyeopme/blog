package me.jaeyeop.blog.comment.application.port.in;

public interface CommentCommandUseCase {

    Long write(WriteCommand command);

    void edit(EditCommand command);

    void delete(DeleteCommand command);

    record WriteCommand(Long authorId, Long targetId, String content) {}

    record EditCommand(Long authorId, Long targetId, String newContent) {}

    record DeleteCommand(Long authorId, Long targetId) {}
}
