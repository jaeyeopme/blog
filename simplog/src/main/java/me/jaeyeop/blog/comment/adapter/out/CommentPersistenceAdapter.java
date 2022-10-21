package me.jaeyeop.blog.comment.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.comment.adapter.out.response.CommentInfo;
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class CommentPersistenceAdapter implements CommentCommandPort, CommentQueryPort {

  private final CommentCrudRepository commentCrudRepository;

  private final CommentQueryRepository commentQueryRepository;

  public CommentPersistenceAdapter(final CommentCrudRepository commentCrudRepository,
      final CommentQueryRepository commentQueryRepository) {
    this.commentCrudRepository = commentCrudRepository;
    this.commentQueryRepository = commentQueryRepository;
  }

  @Override
  public Page<CommentInfo> findPageInfoByPostId(final Long postId,
      final Pageable pageable) {
    return commentQueryRepository.findPageInfoByPostId(postId, pageable);
  }

  @Override
  public Optional<Comment> findById(final Long id) {
    return commentCrudRepository.findById(id);
  }

  @Override
  public void delete(final Comment comment) {
    commentCrudRepository.delete(comment);
  }

}
