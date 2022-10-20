package me.jaeyeop.blog.comment.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.comment.adapter.in.UpdateCommentCommand;
import me.jaeyeop.blog.comment.adapter.in.command.CreateCommentCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CommentCommandService implements CommentCommandUseCase {

  private final CommentQueryPort commentQueryPort;

  private final PostQueryPort postQueryPort;

  public CommentCommandService(final CommentQueryPort commentQueryPort,
      final PostQueryPort postQueryPort) {
    this.commentQueryPort = commentQueryPort;
    this.postQueryPort = postQueryPort;
  }

  @Override
  public void create(final Long authorId,
      final CreateCommentCommand command) {
    final Post post = postQueryPort.findById(command.getPostId())
        .orElseThrow(PostNotFoundException::new);

    final Comment comment = Comment.of(authorId, command.getContent());

    post.addComments(comment);
  }

  @Override
  public void update(final Long authorId,
      final Long commentId,
      final UpdateCommentCommand command) {
    final Comment comment = commentQueryPort.findById(commentId)
        .orElseThrow(CommentNotFoundException::new);

    comment.confirmAccess(authorId);

    comment.updateInformation(command.getContent());
  }

}
