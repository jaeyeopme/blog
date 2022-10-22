package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Delete;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Update;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.PostFactory;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostCommandServiceTest {

  @Mock
  PostCommandPort postCommandPort;

  @Mock(stubOnly = true)
  PostQueryPort postQueryPort;

  @InjectMocks
  private PostCommandService postCommandService;

  @Test
  void 게시글_저장() {
    final var author1 = UserFactory.createUser1();
    final var post1 = PostFactory.createPost1(author1);
    given(postCommandPort.create(any())).willReturn(post1);
    final var postId = post1.id();

    final var actual = postCommandService.create(author1.id(),
        new Create(post1.title(), post1.content()));

    assertThat(actual).isEqualTo(postId);
  }

  @Test
  void 게시글_업데이트() {
    final var id = 1L;
    final var author1 = UserFactory.createUser1();
    final var post1 = PostFactory.createPost1(author1);
    given(postQueryPort.findById(id)).willReturn(Optional.of(post1));
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    final ThrowingCallable when = () -> postCommandService.update(author1.id(), id,
        new Update(newTitle, newContent));

    assertThatNoException().isThrownBy(when);
    assertThat(post1.title()).isEqualTo(newTitle);
    assertThat(post1.content()).isEqualTo(newContent);
  }

  @Test
  void 존재하지_않는_게시글_업데이트() {
    final var id = 1L;
    given(postQueryPort.findById(id)).willReturn(Optional.empty());
    final var authorId = 2L;

    final ThrowingCallable when = () -> postCommandService.update(authorId, id,
        new Update("newTitle", "newContent"));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 다른_사람의_게시글_업데이트() {
    final var id = 1L;
    final var author1 = UserFactory.createUser1();
    final var post1 = PostFactory.createPost1(author1);
    final var author2Id = 99L;
    given(postQueryPort.findById(id)).willReturn(Optional.of(post1));
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    final ThrowingCallable when = () -> postCommandService.update(author2Id, id,
        new Update(newTitle, newContent));

    assertThat(author1.id()).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(post1.title()).isNotEqualTo(newTitle);
    assertThat(post1.content()).isNotEqualTo(newContent);
  }

  @Test
  void 게시글_삭제() {
    final var id = 1L;
    final var author1 = UserFactory.createUser1();
    final var post1 = PostFactory.createPost1(author1);
    given(postQueryPort.findById(id)).willReturn(Optional.of(post1));

    final ThrowingCallable when = () -> postCommandService.delete(author1.id(), new Delete(id));

    assertThatNoException().isThrownBy(when);
    then(postCommandPort).should().delete(post1);
  }

  @Test
  void 존재하지_않는_게시글_삭제() {
    final var id = 1L;
    given(postQueryPort.findById(id)).willReturn(Optional.empty());
    final var authorId = 2L;

    final ThrowingCallable when = () -> postCommandService.delete(authorId, new Delete(id));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    then(postCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_게시글_삭제() {
    final var id = 1L;
    final var author1 = UserFactory.createUser1();
    final var post1 = PostFactory.createPost1(author1);
    given(postQueryPort.findById(id)).willReturn(Optional.of(post1));
    final var author2Id = 99L;

    final ThrowingCallable when = () -> postCommandService.delete(author2Id, new Delete(id));

    assertThat(author1.id()).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(postCommandPort).should(never()).delete(any());
  }

}
