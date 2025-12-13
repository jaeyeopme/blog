package me.jaeyeop.blog.comment.adapter.out;

import java.util.List;
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

    private final CommentJpaRepository commentJpaRepository;

    private final CommentQueryRepository commentQueryRepository;

    public CommentPersistenceAdapter(
            final CommentJpaRepository commentJpaRepository,
            final CommentQueryRepository commentQueryRepository) {
        this.commentJpaRepository = commentJpaRepository;
        this.commentQueryRepository = commentQueryRepository;
    }

    @Override
    public Comment save(final Comment comment) {
        return commentJpaRepository.save(comment);
    }

    @Override
    public void delete(final Comment comment) {
        commentJpaRepository.delete(comment);
    }

    @Override
    public void deleteAll(final List<Comment> comments) {
        commentJpaRepository.deleteAllInBatch(comments);
    }

    @Override
    public Optional<Comment> findById(final Long id) {
        return commentJpaRepository.findById(id);
    }

    @Override
    public Optional<CommentInformationProjectionDto> findInformationById(final Long id) {
        return commentQueryRepository.findInformationById(id);
    }

    @Override
    public Page<CommentInformationProjectionDto> findInformationPageByPostId(
            final Long postId, final Pageable pageable) {
        return commentQueryRepository.findInformationPageByPostId(postId, pageable);
    }
}
