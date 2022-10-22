package me.jaeyeop.blog.post.application.service;

import static me.jaeyeop.blog.post.adapter.in.PostRequest.Find;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
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
  public Info findOne(final Find request) {
    return postQueryPort.findInfoById(request.postId())
        .orElseThrow(PostNotFoundException::new);
  }

}
