package me.jaeyeop.blog.comment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Find;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.CommentFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

@ExtendWith(MockitoExtension.class)
class CommentQueryServiceTest {

  @Mock
  private CommentQueryPort commentQueryPort;

  @InjectMocks
  private CommentQueryService commentQueryService;

  @Test
  void 댓글_페이지_조회() {
    final var postId = 1L;
    final var pageable = PageRequest.of(5, 10, Direction.DESC, "createdAt");
    final var infoPage = CommentFactory.createInfoPage(pageable);
    given(commentQueryPort.findInfoPageByPostId(postId, pageable)).willReturn(infoPage);

    final var actual = commentQueryService.findCommentPage(new Find(postId, pageable));

    assertThat(actual).isEqualTo(infoPage);
  }

}
