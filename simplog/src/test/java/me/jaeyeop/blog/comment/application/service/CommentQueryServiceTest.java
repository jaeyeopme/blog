package me.jaeyeop.blog.comment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.comment.adapter.out.CommentPersistenceAdapter;
import me.jaeyeop.blog.comment.adapter.out.CommentQueryRepository;
import me.jaeyeop.blog.comment.application.port.in.CommentQueryUseCase;
import me.jaeyeop.blog.comment.domain.CommentFactory;
import me.jaeyeop.blog.post.adapter.in.command.GetCommentsCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

class CommentQueryServiceTest {

  private CommentQueryRepository commentQueryRepository;

  private CommentQueryUseCase commentQueryUseCase;

  @BeforeEach
  void setUp() {
    commentQueryRepository = Mockito.mock(CommentQueryRepository.class);
    commentQueryUseCase = new CommentQueryService(
        new CommentPersistenceAdapter(commentQueryRepository));
  }

  @Test
  void 댓글_페이지_조회() {
    final var postId = 1L;
    final var pageable = PageRequest.of(5, 10, Direction.DESC, "createdAt");
    final var command = new GetCommentsCommand(postId, pageable);
    final var expected = CommentFactory.createPageInfo(pageable);
    given(commentQueryRepository.findPageInfoByPostId(postId, pageable)).willReturn(expected);

    final var actual = commentQueryUseCase.getPage(command);

    assertThat(actual).isEqualTo(expected);
  }

}
