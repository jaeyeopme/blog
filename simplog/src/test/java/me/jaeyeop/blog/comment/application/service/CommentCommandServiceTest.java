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
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.CommentHelper;
import me.jaeyeop.blog.support.helper.PostHelper;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.domain.User;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 10/20/2022.
 */
class CommentCommandServiceTest extends UnitTest {

  @Test
  void 댓글_작성() {
    // GIVEN
    final var postId = 1L;
    final var author = getAuthor(22L);
    final var post = getPost(postId, author);
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
    final var author = getAuthor(44L);
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
    final var author = getAuthor(12L);
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
    final var commentId = 2L;
    final var comment = CommentHelper.create(getAuthor(6L));
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));
    final var author = getAuthor(9L);
    final var newContent = "newContent";

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.update(
        author, commentId, new Update(newContent));

    // THEN
    assertThat(comment.author().id()).isNotEqualTo(author.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(comment.content()).isNotEqualTo(newContent);
  }

  @Test
  void 댓글_삭제() {
    // GIVEN
    final var commentId = 1L;
    final var author = getAuthor(22L);
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
    final var author = getAuthor(32L);
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
    final var commentId = 5L;
    final var comment = CommentHelper.create(getAuthor(88L));
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment));
    final var author = getAuthor(77L);

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(
        author, new Delete(commentId));

    // THEN
    assertThat(comment.author().id()).isNotEqualTo(author.id());
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

  private Post getPost(final Long postId, final User author) {
    final var post = PostHelper.create(author);
    ReflectionTestUtils.setField(post, "id", postId);
    return post;
  }

  private User getAuthor(final Long authorId) {
    final var author = UserHelper.create();
    ReflectionTestUtils.setField(author, "id", authorId);
    return author;
  }

}
