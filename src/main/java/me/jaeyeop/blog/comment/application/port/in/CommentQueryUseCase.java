package me.jaeyeop.blog.comment.application.port.in;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;

public interface CommentQueryUseCase {
    CommentInformationProjectionDto findInformationById(Query query);

    Page<CommentInformationProjectionDto> findInformationPageByPostId(PageQuery query);

    record Query(Long commentId) {}

    record PageQuery(Long postId, Pageable pageable) {}
}
