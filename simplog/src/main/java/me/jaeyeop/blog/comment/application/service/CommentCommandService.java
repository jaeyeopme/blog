package me.jaeyeop.blog.comment.application.service;

import javax.transaction.Transactional;
import javax.validation.constraints.NotBlank;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Create;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Delete;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Update;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Service;

/**
 * @author jaeyeopme Created on 10/18/2022.
 */
@Transactional
@Service
public class CommentCommandService implements CommentCommandUseCase {

  private final CommentCommandPort commentCommandPort;

  private final CommentQueryPort commentQueryPort;

  private final PostQueryPort postQueryPort;

  public CommentCommandService(
      final CommentCommandPort commentCommandPort,
      final CommentQueryPort commentQueryPort,
      final PostQueryPort postQueryPort) {
    this.commentCommandPort = commentCommandPort;
    this.commentQueryPort = commentQueryPort;
    this.postQueryPort = postQueryPort;
  }

  @Override
  public void create(
      final User author,
      final Create request) {
    final var post = postQueryPort.findById(request.postId())
        .orElseThrow(PostNotFoundException::new);

    final var comment = Comment.of(request.content(), author);

    post.addComments(comment);
  }

  @Override
  public void update(
      @NotBlank final User author,
      final Long commentId,
      final Update request) {
    final var comment = findById(author.id(), commentId);

    comment.updateInformation(request.content());
  }

  @Override
  public void delete(final User author, final Delete request) {
    final var comment = findById(author.id(), request.commentId());

    commentCommandPort.delete(comment);
  }

  private Comment findById(final Long authorId, final Long commentId) {
    final var comment = commentQueryPort.findById(commentId)
        .orElseThrow(CommentNotFoundException::new);

    comment.confirmAccess(authorId);

    return comment;
  }

}
