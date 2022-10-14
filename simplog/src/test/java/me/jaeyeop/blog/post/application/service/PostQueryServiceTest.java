package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.in.command.GetPostInformationCommand;
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
    final var id = 1L;
    final var command = new GetPostInformationCommand(id);
    final var expectedPostInformation = PostFactory.createInformation();
    given(postQueryRepository.getPostInformationById(id)).willReturn(
        Optional.of(expectedPostInformation));

    final var actualPostInformation = postQueryUseCase.getInformation(command);

    assertThat(actualPostInformation).isEqualTo(expectedPostInformation);
  }

  @Test
  void 존재하지_않는_게시글_조회() {
    final var id = 1L;
    final var command = new GetPostInformationCommand(id);
    given(postQueryRepository.getPostInformationById(id)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> postQueryUseCase.getInformation(command);

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

}