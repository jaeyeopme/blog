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
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.PostHelper;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.domain.User;
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
    final var author = getAuthor(77L);
    final var post = PostHelper.create(author);
    given(postCommandPort.create(any())).willReturn(post);

    // WHEN
    final var savedPost = postCommandService.create(
        author, new Create(post.title(), post.content()));

    // THEN
    assertThat(savedPost).isEqualTo(post.id());
  }

  @Test
  void 게시글_수정() {
    // GIVEN
    final var postId = 1L;
    final var author = getAuthor(54L);
    final var post = getPost(postId, author);
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
    final var author = getAuthor(77L);
    final var post = getPost(postId, author);
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
    final var postId = 2L;
    final var post = getPost(postId, getAuthor(7L));
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var author = getAuthor(5L);
    final var newTitle = "newTitle";
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> postCommandService.update(
        author, postId, new Update(newTitle, newContent));

    // THEN
    assertThat(post.author().id()).isNotEqualTo(author.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(post.title()).isNotEqualTo(newTitle);
    assertThat(post.content()).isNotEqualTo(newContent);
  }

  @Test
  void 게시글_삭제() {
    // GIVEN
    final var postId = 1L;
    final var author = getAuthor(22L);
    final var post = getPost(postId, author);
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
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> postCommandService.delete(
        UserHelper.create(), new Delete(postId));

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    then(postCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_게시글_삭제() {
    // GIVEN
    final var postId = 1L;
    final var post = getPost(postId, getAuthor(8L));
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));
    final var author = getAuthor(9L);

    // WHEN
    final ThrowingCallable when = () -> postCommandService.delete(
        author, new Delete(postId));

    // THEN
    assertThat(post.author().id()).isNotEqualTo(author.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(postCommandPort).should(never()).delete(any());
  }

  private Post getPost(final Long postId, final User author) {
    final var post2 = PostHelper.create(author);
    ReflectionTestUtils.setField(post2, "id", postId);
    return post2;
  }

  private User getAuthor(final Long authorId) {
    final var author = UserHelper.create();
    ReflectionTestUtils.setField(author, "id", authorId);
    return author;
  }

}
