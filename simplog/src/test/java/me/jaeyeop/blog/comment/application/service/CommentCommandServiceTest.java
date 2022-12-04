package me.jaeyeop.blog.comment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.util.Optional;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Create;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Delete;
import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Update;
import me.jaeyeop.blog.config.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.CommentHelper;
import me.jaeyeop.blog.support.helper.PostHelper;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * @author jaeyeopme Created on 10/20/2022.
 */
class CommentCommandServiceTest extends UnitTest {

  @Test
  void 댓글_작성() {
    // GIVEN
    final var postId = 1L;
    final var post = PostHelper.create(postId);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.create(
        1L, new Create(postId, "content"));

    // THEN
    assertThatNoException().isThrownBy(when);
    assertThat(post.comments().size()).isOne();
  }

  @Test
  void 존재하지_않는_게시글에_댓글_작성() {
    // GIVEN
    final var postId = 1L;
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.create(
        1L, new Create(postId, "content"));

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 댓글_수정() {
    // GIVEN
    final var commentId = 1L;
    final var authorId = 1L;
    final var comment = CommentHelper.createWithAuthor(authorId);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.update(
        authorId, commentId, new Update(newContent));

    // THEN
    assertThatNoException().isThrownBy(when);
    assertThat(comment.content()).isEqualTo(newContent);
  }

  @Test
  void 존재하지_않은_댓글_수정() {
    // GIVEN
    final var commentId = 1L;
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.update(
        1L, commentId, new Update("newContent"));

    // THEN
    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
  }

  @Test
  void 다른_사람의_댓글_수정() {
    // GIVEN
    final var commentId = 1L;
    final var author1Id = 1L;
    final var comment = CommentHelper.createWithAuthor(author1Id);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));
    final var author2Id = 99L;
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.update(
        author2Id, commentId, new Update(newContent));

    // THEN
    assertThat(author1Id).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(comment.content()).isNotEqualTo(newContent);
  }

  @Test
  void 댓글_삭제() {
    // GIVEN
    final var commentId = 1L;
    final var authorId = 1L;
    final var comment = CommentHelper.createWithAuthor(authorId);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(
        authorId, new Delete(commentId));

    // THEN
    assertThatNoException().isThrownBy(when);
    then(commentCommandPort).should().delete(any());
  }

  @Test
  void 존재하지_않는_댓글_삭제() {
    // GIVEN
    final var commentId = 1L;
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(
        1L, new Delete(commentId));

    // THEN
    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_댓글_삭제() {
    // GIVEN
    final var commentId = 1L;
    final var author1Id = 1L;
    final var comment = CommentHelper.createWithAuthor(author1Id);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));
    final var author2Id = 99L;

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(
        author2Id, new Delete(commentId));

    // THEN
    assertThat(author1Id).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

}
