package me.jaeyeop.blog.comment.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.util.Optional;
import me.jaeyeop.blog.comment.adapter.in.DeleteCommentCommand;
import me.jaeyeop.blog.comment.adapter.in.UpdateCommentCommand;
import me.jaeyeop.blog.comment.adapter.in.command.CreateCommentCommand;
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
    final var command = new CreateCommentCommand(postId, "content");
    given(postQueryPort.findById(postId)).willReturn(Optional.of(post));

    final ThrowingCallable when = () -> commentCommandService.create(authorId, command);

    assertThatNoException().isThrownBy(when);
    assertThat(post.getComments().size()).isOne();
    assertThat(post.getComments().get(0).getAuthor().getId()).isEqualTo(authorId);
  }

  @Test
  void 존재하지_않는_게시글에_댓글_작성() {
    final var postId = 9L;
    final var authorId = 1L;
    final var command = new CreateCommentCommand(postId, "content");
    given(postQueryPort.findById(postId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> commentCommandService.create(authorId, command);

    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }

  @Test
  void 댓글_수정() {
    final var commentId = 8L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1WithAuthor(author1);
    final var command = new UpdateCommentCommand("newContent");
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));

    final ThrowingCallable when = () -> commentCommandService.update(author1.getId(), commentId,
        command);

    assertThatNoException().isThrownBy(when);
    assertThat(comment1.getContent()).isEqualTo(command.getContent());
  }

  @Test
  void 존재하지_않는_댓글_수정() {
    final var commentId = 1L;
    final var authorId = 1L;
    final var command = new UpdateCommentCommand("newContent");
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> commentCommandService.update(authorId, commentId,
        command);

    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
  }

  @Test
  void 다른_사람의_댓글_수정() {
    final var commentId = 1L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1WithAuthor(author1);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));
    final var author2Id = 99L;
    final var command = new UpdateCommentCommand("newContent");

    final ThrowingCallable when = () -> commentCommandService.update(author2Id, commentId,
        command);

    assertThat(author1.getId()).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    assertThat(comment1.getContent()).isNotEqualTo(command.getContent());
  }

  @Test
  void 댓글_삭제() {
    final var commentId = 8L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1WithAuthor(author1);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));
    final var command = new DeleteCommentCommand(commentId);

    final ThrowingCallable when = () -> commentCommandService.delete(author1.getId(), command);

    assertThatNoException().isThrownBy(when);
    then(commentCommandPort).should().delete(any());
  }

  @Test
  void 존재하지_않는_댓글_삭제() {
    final var commentId = 1L;
    given(commentQueryPort.findById(commentId)).willReturn(Optional.empty());
    final var authorId = 1L;
    final var command = new DeleteCommentCommand(commentId);

    final ThrowingCallable when = () -> commentCommandService.delete(authorId, command);

    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

  @Test
  void 다른_사람의_댓글_삭제() {
    final var commentId = 1L;
    final var author1 = UserFactory.createUser1();
    final var comment1 = CommentFactory.createComment1WithAuthor(author1);
    given(commentQueryPort.findById(commentId)).willReturn(Optional.of(comment1));
    final var author2Id = 99L;
    final var command = new DeleteCommentCommand(commentId);

    final ThrowingCallable when = () -> commentCommandService.delete(author2Id, command);

    assertThat(author1.getId()).isNotEqualTo(author2Id);
    assertThatThrownBy(when).isInstanceOf(PrincipalAccessDeniedException.class);
    then(commentCommandPort).should(never()).delete(any());
  }

}