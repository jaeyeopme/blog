package me.jaeyeop.blog.post.application.service;

import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.in.PostInformationProjectionDto;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaeyeopme Created on 10/12/2022.
 */
@Transactional(readOnly = true)
@Service
public class PostQueryService implements PostQueryUseCase {

  private final PostQueryPort postQueryPort;

  public PostQueryService(final PostQueryPort postQueryPort) {
    this.postQueryPort = postQueryPort;
  }

  @Override
  public PostInformationProjectionDto findInformationById(final InformationQuery informationQuery) {
    return postQueryPort.findInformationById(informationQuery.postId())
        .orElseThrow(PostNotFoundException::new);
  }

}
