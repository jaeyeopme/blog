package me.jaeyeop.blog.comment.application.service;

import me.jaeyeop.blog.comment.adapter.out.response.CommentInfo;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.post.adapter.in.command.GetCommentsCommand;
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
  public Page<CommentInfo> getPage(final GetCommentsCommand command) {
    return commentQueryPort.findPageInfoByPostId(command.getPostId(),
        command.getCommentsPageable());
  }

}
