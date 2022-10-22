package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Find;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.PostFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostQueryServiceTest {

  @Mock
  private PostQueryPort postQueryPort;

  @InjectMocks
  private PostQueryService postQueryService;

  @Test
  void 게시글_조회() {
    final var postId = 1L;
    final var postInfo = PostFactory.createInfo(postId);
    given(postQueryPort.findInfoById(postId)).willReturn(Optional.of(postInfo));

    final var actual = postQueryService.findOne(new Find(postId));

    assertThat(actual).isEqualTo(postInfo);
  }

  @Test
  void 존재하지_않는_게시글_조회() {
    final var postId = 1L;
    given(postQueryPort.findInfoById(postId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> postQueryService.findOne(new Find(postId));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

}
