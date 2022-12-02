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
import me.jaeyeop.blog.support.UnitTestSupport;
import me.jaeyeop.blog.support.helper.PostHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
class PostCommandServiceTest extends UnitTestSupport {

  @Test
  void 게시글_저장() {
    final var postId = 1L;
    final var post = PostHelper.create(postId);
    ReflectionTestUtils.setField(post, "id", postId);
    given(postCommandPort.create(any())).willReturn(post);

    final var actual = postCommandService.create(
        1L, new Create(post.title(), post.content()));

    assertThat(actual).isEqualTo(postId);
  }

  @Test
  void 게시글_업데이트() {
    final var postId = 1L;
    final var authorId = 1L;
    final var post = PostHelper.createWithAuthor(authorId);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    final ThrowingCallable when = () -> postCommandService.update(
        authorId, postId, new Update(newTitle, newContent));

    assertThatNoException().isThrownBy(when);
    assertThat(post.title()).isEqualTo(newTitle);
    assertThat(post.content()).isEqualTo(newContent);
  }

  @Test
  void 존재하지_않은_게시글_업데이트() {
    final var postId = 1L;
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> postCommandService.update(
        1L, postId, new Update("newTitle", "newContent"));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 다른_사람의_게시글_업데이트() {
    final var postId = 1L;
    final var author1Id = 1L;
    final var post = PostHelper.createWithAuthor(author1Id);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var author2Id = 99L;
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    final ThrowingCallable when = () -> postCommandService.update(
        author2Id, postId, new Update(newTitle, newContent));

    assertThat(author1Id).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(post.title()).isNotEqualTo(newTitle);
    assertThat(post.content()).isNotEqualTo(newContent);
  }

  @Test
  void 게시글_삭제() {
    final var postId = 1L;
    final var authorId = 1L;
    final var post = PostHelper.createWithAuthor(authorId);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));

    final ThrowingCallable when = () -> postCommandService.delete(
        authorId, new Delete(postId));

    assertThatNoException().isThrownBy(when);
    then(postCommandPort).should().delete(post);
  }

  @Test
  void 존재하지_않은_게시글_삭제() {
    final var postId = 1L;
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> postCommandService.delete(
        2L, new Delete(postId));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    then(postCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_게시글_삭제() {
    final var postId = 1L;
    final var author1Id = 1L;
    final var post = PostHelper.createWithAuthor(author1Id);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var author2Id = 99L;

    final ThrowingCallable when = () -> postCommandService.delete(
        author2Id, new Delete(postId));

    assertThat(author1Id).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(postCommandPort).should(never()).delete(any());
  }

}
