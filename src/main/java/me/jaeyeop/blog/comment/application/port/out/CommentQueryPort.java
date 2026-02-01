package me.jaeyeop.blog.comment.application.port.out;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import me.jaeyeop.blog.comment.domain.Comment;

public interface CommentQueryPort {
    Optional<Comment> findById(Long id);

    Optional<CommentInformationProjectionDto> findInformationById(Long id);

    Page<CommentInformationProjectionDto> findInformationPageByPostId(
            Long postId, Pageable pageable);
}
