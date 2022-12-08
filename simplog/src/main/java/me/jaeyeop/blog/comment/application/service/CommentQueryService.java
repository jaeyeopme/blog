package me.jaeyeop.blog.comment.application.service;

import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Find;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CommentQueryService implements CommentQueryUseCase {

  private final CommentQueryPort commentQueryPort;

  private final PostQueryPort postQueryPort;

  public CommentQueryService(
      final CommentQueryPort commentQueryPort,
      final PostQueryPort postQueryPort) {
    this.commentQueryPort = commentQueryPort;
    this.postQueryPort = postQueryPort;
  }

  @Override
  public Page<Info> findCommentPage(final Find request) {
    validatePost(request.postId());
    return commentQueryPort.findInfoPageByPostId(
        request.postId(),
        request.pageable());
  }

  private void validatePost(final Long postId) {
    if (!postQueryPort.existsById(postId)) {
      throw new PostNotFoundException();
    }
  }

}
