package me.jaeyeop.blog.comment.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
@Component
public class CommentPersistenceAdapter implements CommentCommandPort, CommentQueryPort {

  private final CommentCrudRepository commentCrudRepository;

  private final CommentQueryRepository commentQueryRepository;

  public CommentPersistenceAdapter(
      final CommentCrudRepository commentCrudRepository,
      final CommentQueryRepository commentQueryRepository
  ) {
    this.commentCrudRepository = commentCrudRepository;
    this.commentQueryRepository = commentQueryRepository;
  }

  @Override
  public Comment save(final Comment comment) {
    return commentCrudRepository.save(comment);
  }

  @Override
  public void delete(final Comment comment) {
    commentCrudRepository.delete(comment);
  }

  @Override
  public Optional<Comment> findById(final Long id) {
    return commentCrudRepository.findById(id);
  }

  @Override
  public Optional<CommentInformationProjectionDto> findInformationById(final Long id) {
    return commentQueryRepository.findInformationById(id);
  }

  @Override
  public Page<CommentInformationProjectionDto> findInformationPageByPostId(
      final Long postId,
      final Pageable pageable
  ) {
    return commentQueryRepository.findInformationPageByPostId(postId, pageable);
  }

}
