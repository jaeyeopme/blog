package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.in.command.GetPostCommand;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.adapter.out.PostPersistenceAdapter;
import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase;
import me.jaeyeop.blog.post.domain.PostFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostQueryServiceTest {

  private PostQueryRepository postQueryRepository;

  private PostQueryUseCase postQueryUseCase;

  @BeforeEach
  void setUp() {
    postQueryRepository = Mockito.mock(PostQueryRepository.class);
    final var postQueryPort = new PostPersistenceAdapter(
        Mockito.mock(PostCrudRepository.class),
        postQueryRepository);
    postQueryUseCase = new PostQueryService(postQueryPort);
  }

  @Test
  void 게시글_조회() {
    final var postId = 1L;
    final var command = new GetPostCommand(postId);
    final var expected = PostFactory.createInfo(postId);
    given(postQueryRepository.findInfoById(postId)).willReturn(
        Optional.of(expected));

    final var actual = postQueryUseCase.getOne(command);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 존재하지_않는_게시글_조회() {
    final var postId = 1L;
    final var command = new GetPostCommand(postId);
    given(postQueryRepository.findInfoById(postId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> postQueryUseCase.getOne(command);

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

}
