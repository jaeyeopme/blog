package me.jaeyeop.blog.comment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
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
import me.jaeyeop.blog.support.helper.UserHelper;
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
    final var author = UserHelper.create();
    final var post = PostHelper.create(author);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));

    // WHEN
    commentCommandService.create(author, new Create(postId, "content"));

    // THEN
    assertThat(post.comments().size()).isOne();
  }

  @Test
  void 존재하지_않는_게시글에_댓글_작성() {
    // GIVEN
    final var postId = 1L;
    final var author = UserHelper.create();
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.create(
        author, new Create(postId, "content"));

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 댓글_수정() {
    // GIVEN
    final var commentId = 1L;
    final var author = UserHelper.create();
    final var comment = CommentHelper.create(author);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));
    final var newContent = "newContent";

    // WHEN
    commentCommandService.update(author, commentId, new Update(newContent));

    // THEN
    assertThat(comment.content()).isEqualTo(newContent);
  }

  @Test
  void 존재하지_않은_댓글_수정() {
    // GIVEN
    final var commentId = 1L;
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.update(
        UserHelper.create(), commentId, new Update("newContent"));

    // THEN
    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
  }

  @Test
  void 다른_사람의_댓글_수정() {
    // GIVEN
    final var comment2Id = 2L;
    final var author2 = UserHelper.create(6L);
    final var comment2 = CommentHelper.create(author2);
    given(commentQueryPort.findById(comment2Id)).willReturn(Optional.of(comment2));
    final var author1 = UserHelper.create(9L);
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.update(
        author1, comment2Id, new Update(newContent));

    // THEN
    assertThat(author2.id()).isNotEqualTo(author1.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(comment2.content()).isNotEqualTo(newContent);
  }

  @Test
  void 댓글_삭제() {
    // GIVEN
    final var commentId = 1L;
    final var author = UserHelper.create();
    final var comment = CommentHelper.create(author);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));

    // WHEN
    commentCommandService.delete(author, new Delete(commentId));

    // THEN
    then(commentCommandPort).should().delete(any());
  }

  @Test
  void 존재하지_않는_댓글_삭제() {
    // GIVEN
    final var commentId = 1L;
    final var author = UserHelper.create();
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(
        author, new Delete(commentId));

    // THEN
    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_댓글_삭제() {
    // GIVEN
    final var comment2Id = 5L;
    final var author2 = UserHelper.create(88L);
    final var comment2 = CommentHelper.create(author2);
    given(commentQueryPort.findById(comment2Id)).willReturn(Optional.of(comment2));
    final var author1 = UserHelper.create(77L);

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(
        author1, new Delete(comment2Id));

    // THEN
    assertThat(author2.id()).isNotEqualTo(author1.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

}
