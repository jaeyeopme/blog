package me.jaeyeop.blog.comment.application.service;

import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Find;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class CommentQueryService implements CommentQueryUseCase {

  private final CommentQueryPort commentQueryPort;

  public CommentQueryService(final CommentQueryPort commentQueryPort) {
    this.commentQueryPort = commentQueryPort;
  }

  @Override
  public Page<Info> findCommentPage(final Find request) {
    return commentQueryPort.findInfoPageByPostId(request.postId(),
        request.commentPageable());
  }

}
