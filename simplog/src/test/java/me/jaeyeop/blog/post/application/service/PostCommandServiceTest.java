package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import me.jaeyeop.blog.support.helper.UserHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
class PostCommandServiceTest extends UnitTest {

  @Test
  void 게시글_저장() {
    // GIVEN
    final var author = UserHelper.create();
    final var post = PostHelper.create(author);
    given(postCommandPort.create(any())).willReturn(post);

    // WHEN
    final var actual = postCommandService.create(
        author, new Create(post.title(), post.content()));

    // THEN
    assertThat(actual).isEqualTo(post.id());
  }

  @Test
  void 게시글_수정() {
    // GIVEN
    final var postId = 1L;
    final var author = UserHelper.create();
    final var post = PostHelper.create(author);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    // WHEN
    postCommandService.update(author, postId, new Update(newTitle, newContent));

    // THEN
    assertThat(post.title()).isEqualTo(newTitle);
    assertThat(post.content()).isEqualTo(newContent);
  }

  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_제목으로_게시글_수정(final String newTitle) {
    // GIVEN
    final var postId = 1L;
    final var author = UserHelper.create();
    final var post = PostHelper.create(author);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var newContent = "newContent";

    // WHEN
    postCommandService.update(author, postId, new Update(newTitle, newContent));

    // THEN
    assertThat(post.title()).isNotEqualTo(newTitle);
    assertThat(post.content()).isEqualTo(newContent);
  }

  @Test
  void 존재하지_않은_게시글_수정() {
    // GIVEN
    final var postId = 1L;
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> postCommandService.update(
        UserHelper.create(), postId, new Update("newTitle", "newContent"));

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 다른_사람의_게시글_수정() {
    // GIVEN
    final var post2Id = 2L;
    final var author2 = UserHelper.create(7L);
    final var post2 = PostHelper.create(author2);
    given(postQueryPort.findById(post2Id)).willReturn(Optional.of(post2));
    final var author1 = UserHelper.create(5L);
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> postCommandService.update(
        author1, post2Id, new Update(newTitle, newContent));

    // THEN
    assertThat(author2.id()).isNotEqualTo(author1.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(post2.title()).isNotEqualTo(newTitle);
    assertThat(post2.content()).isNotEqualTo(newContent);
  }

  @Test
  void 게시글_삭제() {
    // GIVEN
    final var postId = 1L;
    final var author = UserHelper.create();
    final var post = PostHelper.create(author);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));

    // WHEN
    postCommandService.delete(author, new Delete(postId));

    // THEN
    then(postCommandPort).should().delete(post);
  }

  @Test
  void 존재하지_않은_게시글_삭제() {
    // GIVEN
    final var postId = 1L;
    final var author = UserHelper.create();
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> postCommandService.delete(
        author, new Delete(postId));

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    then(postCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_게시글_삭제() {
    // GIVEN
    final var post2Id = 1L;
    final var author2 = UserHelper.create(8L);
    final var post2 = PostHelper.create(author2);
    given(postQueryPort.findById(post2Id)).willReturn(Optional.of(post2));
    final var author1 = UserHelper.create(9L);

    // WHEN
    final ThrowingCallable when = () -> postCommandService.delete(
        author1, new Delete(post2Id));

    // THEN
    assertThat(author2.id()).isNotEqualTo(author1.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(postCommandPort).should(never()).delete(any());
  }

}
