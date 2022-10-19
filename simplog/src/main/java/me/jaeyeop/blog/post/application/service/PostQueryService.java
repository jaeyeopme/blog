package me.jaeyeop.blog.post.application.service;

import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.in.command.GetPostCommand;
import me.jaeyeop.blog.post.adapter.out.response.PostInfo;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class PostQueryService implements PostQueryUseCase {

  private final PostQueryPort postQueryPort;

  public PostQueryService(final PostQueryPort postQueryPort) {
    this.postQueryPort = postQueryPort;
  }

  @Override
  public PostInfo getOne(final GetPostCommand command) {
    return postQueryPort.findInfoById(command.getPostId())
        .orElseThrow(PostNotFoundException::new);
  }

}
