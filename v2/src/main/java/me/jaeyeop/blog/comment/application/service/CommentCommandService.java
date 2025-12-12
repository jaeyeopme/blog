package me.jaeyeop.blog.comment.application.service;

import jakarta.transaction.Transactional;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.comment.domain.CommentInformation;
import me.jaeyeop.blog.commons.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
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

  private final UserQueryPort userQueryPort;

  public CommentCommandService(
      final CommentCommandPort commentCommandPort,
      final CommentQueryPort commentQueryPort,
      final PostQueryPort postQueryPort,
      final UserQueryPort userQueryPort
  ) {
    this.commentCommandPort = commentCommandPort;
    this.commentQueryPort = commentQueryPort;
    this.postQueryPort = postQueryPort;
    this.userQueryPort = userQueryPort;
  }

  @Override
  public Long write(final WriteCommand command) {
    final var post = findPostByPostId(command.targetId());
    final var author = findAuthorByAuthorId(command.authorId());
    final var information = new CommentInformation(command.content());
    final var comment = commentCommandPort.save(Comment.of(post, author, information));
    return comment.id();
  }

  @Override
  public void edit(final EditCommand command) {
    final var comment = findById(command.targetId());
    comment.confirmAccess(findAuthorByAuthorId(command.authorId()));
    comment.information().edit(command.newContent());
  }

  @Override
  public void delete(final DeleteCommand command) {
    final var comment = findById(command.targetId());
    comment.confirmAccess(findAuthorByAuthorId(command.authorId()));
    commentCommandPort.delete(comment);
  }

  private Comment findById(final Long commentId) {
    return commentQueryPort.findById(commentId)
        .orElseThrow(CommentNotFoundException::new);
  }

  private User findAuthorByAuthorId(final Long authorId) {
    return userQueryPort.findById(authorId)
        .orElseThrow(UserNotFoundException::new);
  }

  private Post findPostByPostId(final Long postId) {
    return postQueryPort.findById(postId)
        .orElseThrow(PostNotFoundException::new);
  }

}
