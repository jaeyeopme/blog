package me.jaeyeop.blog.unit.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.util.Optional;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.DeleteCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.EditCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.WriteCommand;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.config.error.exception.AccessDeniedException;
import me.jaeyeop.blog.config.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.CommentHelper;
import me.jaeyeop.blog.support.helper.PostHelper;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.domain.User;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 10/20/2022.
 */
class CommentCommandServiceTest extends UnitTest {

  @Test
  void 댓글_작성() {
    // GIVEN
    final var stubComment = getStubComment(
        52L,
        getStubPost(1L),
        getStubAuthor(24L)
    );
    given(postQueryPort.findById(stubComment.post().id())).willReturn(
        Optional.of(stubComment.post()));
    given(userQueryPort.findById(stubComment.author().id())).willReturn(
        Optional.of(stubComment.author()));
    given(commentCommandPort.save(any(Comment.class)))
        .willReturn(stubComment);
    final var command = new WriteCommand(
        stubComment.author().id(), stubComment.post().id(), stubComment.information().content());

    // WHEN
    final var commentId = commentCommandService.write(command);

    // THEN
    assertThat(commentId).isEqualTo(stubComment.id());
  }

  @Test
  void 존재하지_않는_게시글에_댓글_작성() {
    // GIVEN
    final var command = new WriteCommand(2L, 5L, "content");
    given(postQueryPort.findById(command.targetId())).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.write(command);

    // THEN
    assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
  }


  @Test
  void 댓글_수정() {
    // GIVEN
    final var stubComment = getStubComment(
        8L,
        getStubPost(11L),
        getStubAuthor(12L)
    );
    given(userQueryPort.findById(stubComment.author().id())).willReturn(
        Optional.of(stubComment.author()));
    given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));
    final var command = new EditCommand(stubComment.author().id(), stubComment.id(), "newContent");

    // WHEN
    commentCommandService.edit(command);

    // THEN
    assertThat(stubComment.information().content()).isEqualTo(command.newContent());
  }

  @Test
  void 존재하지_않은_댓글_수정() {
    // GIVEN
    final var command = new EditCommand(51L, 1L, "newContent");
    given(commentQueryPort.findById(command.targetId())).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.edit(command);

    // THEN
    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
  }

  @Test
  void 다른_사람의_댓글_수정() {
    // GIVEN
    final var stubComment = getStubComment(
        8L,
        getStubPost(11L),
        getStubAuthor(88L)
    );
    given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));

    final var stubAuthor = getStubAuthor(12L);
    given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
    final var command = new EditCommand(stubAuthor.id(), stubComment.id(), "newContent");

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.edit(command);

    // THEN
    assertThat(stubComment.author().id()).isNotEqualTo(command.authorId());
    assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
    assertThat(stubComment.information().content()).isNotEqualTo(command.newContent());
  }

  @Test
  void 댓글_삭제() {
    // GIVEN
    final var stubComment = getStubComment(
        56L,
        getStubPost(65L),
        getStubAuthor(6L)
    );
    given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));
    given(userQueryPort.findById(stubComment.author().id())).willReturn(
        Optional.of(stubComment.author()));
    final var command = new DeleteCommand(stubComment.author().id(), stubComment.id());

    // WHEN
    commentCommandService.delete(command);

    // THEN
    then(commentCommandPort).should().delete(any(Comment.class));
  }

  @Test
  void 존재하지_않는_댓글_삭제() {
    // GIVEN
    final var command = new DeleteCommand(5L, 1L);
    given(commentQueryPort.findById(command.targetId())).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(command);

    // THEN
    assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
    then(commentCommandPort).should(never()).delete(any(Comment.class));
  }

  @Test
  void 다른_사람의_댓글_삭제() {
    // GIVEN
    final var stubComment = getStubComment(
        8L,
        getStubPost(11L),
        getStubAuthor(88L)
    );
    given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));

    final var stubAuthor = getStubAuthor(12L);
    given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
    final var command = new DeleteCommand(stubAuthor.id(), stubComment.id());

    // WHEN
    final ThrowingCallable when = () -> commentCommandService.delete(command);

    // THEN
    assertThat(stubComment.author().id()).isNotEqualTo(command.authorId());
    assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
    then(commentCommandPort).should(never()).delete(any(Comment.class));
  }

  private Comment getStubComment(final Long commentId, final Post post, final User author) {
    final var comment = CommentHelper.create(post, author);
    ReflectionTestUtils.setField(comment, "id", commentId);
    return comment;
  }

  private Post getStubPost(final Long postId) {
    final var post = PostHelper.create();
    ReflectionTestUtils.setField(post, "id", postId);
    return post;
  }

  private User getStubAuthor(final Long authorId) {
    final var author = UserHelper.create();
    ReflectionTestUtils.setField(author, "id", authorId);
    return author;
  }

}
