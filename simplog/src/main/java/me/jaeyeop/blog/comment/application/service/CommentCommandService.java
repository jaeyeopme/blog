package me.jaeyeop.blog.comment.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.comment.adapter.in.CreateCommentCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class CommentCommandService implements CommentCommandUseCase {

  private final PostQueryPort postQueryPort;

  public CommentCommandService(final PostQueryPort postQueryPort) {
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

}
