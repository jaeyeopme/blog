package me.jaeyeop.blog.comment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Find;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.support.UnitTestSupport;
import me.jaeyeop.blog.support.helper.CommentHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
class CommentQueryServiceTest extends UnitTestSupport {

  @Test
  void 댓글_페이지_조회() {
    final var postId = 1L;
    final var pageable = getPageable();
    final var infoPage = CommentHelper.createInfoPage(pageable);
    given(postQueryPort.existsById(postId)).willReturn(Boolean.TRUE);
    given(commentQueryPort.findInfoPageByPostId(postId, pageable)).willReturn(infoPage);

    final var actual = commentQueryService.findCommentPage(
        new Find(postId, pageable));

    assertThat(actual).isEqualTo(infoPage);
  }

  @Test
  void 존재하지_않은_게시글의_댓글_페이지_조회() {
    final var postId = 1L;
    final var pageable = getPageable();
    given(postQueryPort.existsById(postId)).willReturn(Boolean.FALSE);

    final ThrowingCallable when = () -> commentQueryService.findCommentPage(
        new Find(postId, pageable));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  private PageRequest getPageable() {
    return PageRequest.of(5, 10, Direction.DESC, "createdAt");
  }

}
