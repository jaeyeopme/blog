package me.jaeyeop.blog.comment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Find;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.CommentFactory;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class CommentQueryServiceTest {

  @Mock(stubOnly = true)
  private CommentQueryPort commentQueryPort;

  @Mock(stubOnly = true)
  private PostQueryPort postQueryPort;

  @InjectMocks
  private CommentQueryService commentQueryService;

  @Test
  void 댓글_페이지_조회() {
    final var postId = 1L;
    final var pageable = PageRequest.of(5, 10, Direction.DESC, "createdAt");
    final var infoPage = CommentFactory.createInfoPage(pageable);
    given(postQueryPort.existsById(postId)).willReturn(Boolean.TRUE);
    given(commentQueryPort.findInfoPageByPostId(postId, pageable)).willReturn(infoPage);

    final var actual = commentQueryService.findCommentPage(new Find(postId, pageable));

    assertThat(actual).isEqualTo(infoPage);
  }

  @Test
  void 존재하지_않은_게시글의_댓글_페이지_조회() {
    final var postId = 1L;
    final var pageable = PageRequest.of(5, 10, Direction.DESC, "createdAt");
    given(postQueryPort.existsById(postId)).willReturn(Boolean.FALSE);

    final ThrowingCallable when = () -> commentQueryService.findCommentPage(
        new Find(postId, pageable));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

}
