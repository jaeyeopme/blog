package me.jaeyeop.blog.comment.application.service;

import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.config.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
@Transactional(readOnly = true)
@Service
public class CommentQueryService implements CommentQueryUseCase {

  private final CommentQueryPort commentQueryPort;

  private final PostQueryPort postQueryPort;

  public CommentQueryService(
      final CommentQueryPort commentQueryPort,
      final PostQueryPort postQueryPort
  ) {
    this.commentQueryPort = commentQueryPort;
    this.postQueryPort = postQueryPort;
  }

  @Override
  public CommentInformationProjectionDto findInformationById(final Query query) {
    return commentQueryPort.findInformationById(query.commentId())
        .orElseThrow(CommentNotFoundException::new);
  }

  @Override
  public Page<CommentInformationProjectionDto> findInformationPageByPostId(final PageQuery query) {
    if (!postQueryPort.existsById(query.postId())) {
      throw new PostNotFoundException();
    }

    return commentQueryPort.findInformationPageByPostId(
        query.postId(), query.pageable());
  }

}
