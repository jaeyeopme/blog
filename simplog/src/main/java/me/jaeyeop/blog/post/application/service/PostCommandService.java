package me.jaeyeop.blog.post.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class PostCommandService implements PostCommandUseCase {

  private final PostCommandPort postCommandPort;

  public PostCommandService(final PostCommandPort postCommandPort) {
    this.postCommandPort = postCommandPort;
  }

  @Override
  public Long create(final Long authorId,
      final CreatePostCommand command) {
    final Post post = Post.of(authorId, command.getTitle(), command.getContent());
    return postCommandPort.create(post).getId();
  }

}
