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
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.domain.CommentFactory;
import me.jaeyeop.blog.config.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.config.error.exception.PrincipalAccessDeniedException;
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
class CommentCommandServiceTest {

  @Mock
  private CommentCommandPort commentCommandPort;

  @Mock(stubOnly = true)
  private CommentQueryPort commentQueryPort;

  @Mock
  private PostQueryPort postQueryPort;

  @InjectMocks
  private CommentCommandService commentCommandService;

  @Test
  void 댓글_작성() {
    final var postId = 9L;
    final var authorId = 1L;
    final var post = PostFactory.createPost(postId);
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));

    final ThrowingCallable when = () -> commentCommandService.create(authorId,
        new Create(postId, "content"));

    assertThatNoException().isThrownBy(when);
    assertThat(post.comments().size()).isOne();
    assertThat(post.comments().get(0).author().id()).isEqualTo(authorId);
  }

  @Test
  void 존재하지_않는_게시글에_댓글_작성() {
    final var postId = 9L;
    final var authorId = 1L;
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> commentCommandService.create(authorId,
        new Create(postId, "content"));

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 댓글_수정() {
    final var commentId = 8L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1(author1);
    final var newContent = "newContent";
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));

    final ThrowingCallable when = () -> commentCommandService.update(author1.id(), commentId,
        new Update(newContent));

    assertThatNoException().isThrownBy(when);
    assertThat(comment1.content()).isEqualTo(newContent);
  }

  @Test
  void 존재하지_않는_댓글_수정() {
    final var commentId = 1L;
    final var authorId = 1L;
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> commentCommandService.update(authorId, commentId,
        new Update("newContent"));

    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
  }

  @Test
  void 다른_사람의_댓글_수정() {
    final var commentId = 1L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1(author1);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));
    final var author2Id = 99L;
    final var newContent = "newContent";

    final ThrowingCallable when = () -> commentCommandService.update(author2Id, commentId,
        new Update(newContent));

    assertThat(author1.id()).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(comment1.content()).isNotEqualTo(newContent);
  }

  @Test
  void 댓글_삭제() {
    final var commentId = 8L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1(author1);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));

    final ThrowingCallable when = () -> commentCommandService.delete(author1.id(),
        new Delete(commentId));

    assertThatNoException().isThrownBy(when);
    then(commentCommandPort).should().delete(any());
  }

  @Test
  void 존재하지_않는_댓글_삭제() {
    final var commentId = 1L;
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());
    final var authorId = 1L;

    final ThrowingCallable when = () -> commentCommandService.delete(authorId,
        new Delete(commentId));

    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_댓글_삭제() {
    final var commentId = 1L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1(author1);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));
    final var author2Id = 99L;

    final ThrowingCallable when = () -> commentCommandService.delete(author2Id,
        new Delete(commentId));

    assertThat(author1.id()).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

}