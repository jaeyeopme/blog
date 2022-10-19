package me.jaeyeop.blog.comment.adapter.out;

import me.jaeyeop.blog.comment.adapter.out.response.CommentInfo;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CommentPersistenceAdapter implements CommentQueryPort {

  private final CommentQueryRepository commentQueryRepository;

  public CommentPersistenceAdapter(final CommentQueryRepository commentQueryRepository) {
    this.commentQueryRepository = commentQueryRepository;
  }

  @Override
  public Page<CommentInfo> findPageInfoByPostId(final Long postId,
      final Pageable pageable) {
    return commentQueryRepository.findPageInfoByPostId(postId, pageable);
  }

}
