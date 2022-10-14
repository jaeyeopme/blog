package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.adapter.in.command.UpdatePostCommand;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.adapter.out.PostPersistenceAdapter;
import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.domain.PostFactory;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostCommandServiceTest {

  private PostCrudRepository postCrudRepository;

  private PostCommandUseCase postCommandUseCase;

  @BeforeEach
  void setUp() {
    postCrudRepository = Mockito.mock(PostCrudRepository.class);
    final var postPersistencePort = new PostPersistenceAdapter(
        postCrudRepository,
        Mockito.mock(PostQueryRepository.class));
    postCommandUseCase = new PostCommandService(postPersistencePort, postPersistencePort);
  }

  @Test
  void 게시글_저장() {
    final var author1 = UserFactory.createUser1();
    final var post = PostFactory.createPost1(author1);
    final var command = new CreatePostCommand(post.getTitle(), post.getContent());
    final var expectedId = post.getId();
    given(postCrudRepository.save(any())).willReturn(post);

    final var actualId = postCommandUseCase.create(author1.getId(), command);

    assertThat(actualId).isEqualTo(expectedId);
  }

  @Test
  void 게시글_업데이트() {
    final var id = 1L;
    final var command = new UpdatePostCommand("newTitle", "newContent");
    final var author1 = UserFactory.createUser1();
    final var post = PostFactory.createPost1(author1);
    given(postCrudRepository.findById(id)).willReturn(Optional.of(post));

    postCommandUseCase.update(author1.getId(), id, command);

    assertThat(post.getTitle()).isEqualTo(command.getTitle());
    assertThat(post.getContent()).isEqualTo(command.getContent());
  }

  @Test
  void 존재하지_않는_게시글_업데이트() {
    final var id = 1L;
    final var command = new UpdatePostCommand("newTitle", "newContent");
    given(postCrudRepository.findById(id)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> postCommandUseCase.update(1L, id, command);

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 다른_사람의_게시글_업데이트() {
    final var id = 1L;
    final var command = new UpdatePostCommand("newTitle", "newContent");
    final var author1 = UserFactory.createUser1();
    final var post = PostFactory.createPost1(author1);
    final var author2 = UserFactory.createUser2();
    given(postCrudRepository.findById(id)).willReturn(Optional.of(post));

    final ThrowingCallable when = () -> postCommandUseCase.update(author2.getId(), id, command);

    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
  }

}
