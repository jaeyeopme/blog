package me.jaeyeop.blog.post.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.adapter.in.command.UpdatePostCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class PostCommandService implements PostCommandUseCase {

  private final PostCommandPort postCommandPort;

  private final PostQueryPort postQueryPort;

  public PostCommandService(final PostCommandPort postCommandPort,
      final PostQueryPort postQueryPort) {
    this.postCommandPort = postCommandPort;
    this.postQueryPort = postQueryPort;
  }

  @Override
  public Long create(final Long authorId,
      final CreatePostCommand command) {
    final Post post = Post.of(authorId, command.getTitle(), command.getContent());
    return postCommandPort.create(post).getId();
  }

  @Override
  public void update(final Long authorId,
      final Long postId,
      final UpdatePostCommand command) {
    final Post post = postQueryPort.findById(postId)
        .orElseThrow(PostNotFoundException::new);

    if (post.isInaccessible(authorId)) {
      throw new PrincipalAccessDeniedException();
    }

    post.updateInformation(command.getTitle(), command.getContent());
  }

}
