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
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.PostHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
class PostCommandServiceTest extends UnitTest {

  @Test
  void 게시글_저장() {
    // GIVEN
    final var postId = 1L;
    final var post = PostHelper.create(postId);
    ReflectionTestUtils.setField(post, "id", postId);
    given(postCommandPort.create(any())).willReturn(post);

    // WHEN
    final var actual = postCommandService.create(
        1L, new Create(post.title(), post.content()));

    // THEN
    assertThat(actual).isEqualTo(postId);
  }

  @Test
  void 게시글_수정() {
    // GIVEN
    final var postId = 1L;
    final var authorId = 1L;
    final var post = PostHelper.createWithAuthor(authorId);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> postCommandService.update(
        authorId, postId, new Update(newTitle, newContent));

    // THEN
    assertThatNoException().isThrownBy(when);
    assertThat(post.title()).isEqualTo(newTitle);
    assertThat(post.content()).isEqualTo(newContent);
  }

  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_제목으로_게시글_수정(final String newTitle) {
    // GIVEN
    final var postId = 1L;
    final var authorId = 1L;
    final var post = PostHelper.createWithAuthor(authorId);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> postCommandService.update(
        authorId, postId, new Update(newTitle, newContent));

    // THEN
    assertThatNoException().isThrownBy(when);
    assertThat(post.title()).isEqualTo(post.title());
    assertThat(post.content()).isEqualTo(newContent);
  }

  @Test
  void 존재하지_않은_게시글_수정() {
    // GIVEN
    final var postId = 1L;
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> postCommandService.update(
        1L, postId, new Update("newTitle", "newContent"));

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 다른_사람의_게시글_수정() {
    // GIVEN
    final var postId = 1L;
    final var author1Id = 1L;
    final var post = PostHelper.createWithAuthor(author1Id);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var author2Id = 99L;
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> postCommandService.update(
        author2Id, postId, new Update(newTitle, newContent));

    // THEN
    assertThat(author1Id).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(post.title()).isNotEqualTo(newTitle);
    assertThat(post.content()).isNotEqualTo(newContent);
  }

  @Test
  void 게시글_삭제() {
    // GIVEN
    final var postId = 1L;
    final var authorId = 1L;
    final var post = PostHelper.createWithAuthor(authorId);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));

    // WHEN
    final ThrowingCallable when = () -> postCommandService.delete(
        authorId, new Delete(postId));

    // THEN
    assertThatNoException().isThrownBy(when);
    then(postCommandPort).should().delete(post);
  }

  @Test
  void 존재하지_않은_게시글_삭제() {
    // GIVEN
    final var postId = 1L;
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> postCommandService.delete(
        2L, new Delete(postId));

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    then(postCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_게시글_삭제() {
    // GIVEN
    final var postId = 1L;
    final var author1Id = 1L;
    final var post = PostHelper.createWithAuthor(author1Id);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var author2Id = 99L;

    // WHEN
    final ThrowingCallable when = () -> postCommandService.delete(
        author2Id, new Delete(postId));

    // THEN
    assertThat(author1Id).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(postCommandPort).should(never()).delete(any());
  }

}
