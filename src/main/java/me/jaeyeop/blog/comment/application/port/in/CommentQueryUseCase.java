package me.jaeyeop.blog.comment.application.port.in;

import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
public interface CommentQueryUseCase {

    CommentInformationProjectionDto findInformationById(Query query);

    Page<CommentInformationProjectionDto> findInformationPageByPostId(PageQuery query);

    record Query(Long commentId) {}

    record PageQuery(Long postId, Pageable pageable) {}
}
