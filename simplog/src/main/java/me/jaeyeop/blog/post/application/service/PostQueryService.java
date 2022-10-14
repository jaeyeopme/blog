package me.jaeyeop.blog.post.application.service;

import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.in.command.GetPostInformationCommand;
import me.jaeyeop.blog.post.adapter.in.response.PostInformation;
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
  public PostInformation getInformation(final GetPostInformationCommand command) {
    return postQueryPort.getPostInformationById(command.getId())
        .orElseThrow(PostNotFoundException::new);
  }

}
