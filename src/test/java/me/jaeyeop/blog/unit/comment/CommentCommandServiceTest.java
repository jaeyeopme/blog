package me.jaeyeop.blog.unit.comment;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.DeleteCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.EditCommand;
import me.jaeyeop.blog.comment.application.port.in.CommentCommandUseCase.WriteCommand;
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.application.service.CommentCommandService;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.commons.error.exception.AccessDeniedException;
import me.jaeyeop.blog.commons.error.exception.CommentNotFoundException;
import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.CommentFactory;
import me.jaeyeop.blog.support.factory.PostFactory;
import me.jaeyeop.blog.support.factory.UserFactory;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class CommentCommandServiceTest extends UnitTest {
    @InjectMocks
    private CommentCommandService commentCommandService;

    @Mock
    private CommentCommandPort commentCommandPort;

    @Mock(stubOnly = true)
    private CommentQueryPort commentQueryPort;

    @Mock(stubOnly = true)
    private PostQueryPort postQueryPort;

    @Mock(stubOnly = true)
    private UserQueryPort userQueryPort;

    @Test
    @DisplayName("Write comment")
    void write_comment() {
        final var stubComment = getStubComment(52L, getStubPost(1L), getStubAuthor(24L));
        given(postQueryPort.findById(stubComment.post().id()))
                .willReturn(Optional.of(stubComment.post()));
        given(userQueryPort.findById(stubComment.author().id()))
                .willReturn(Optional.of(stubComment.author()));
        given(commentCommandPort.save(any(Comment.class))).willReturn(stubComment);
        final var command =
                new WriteCommand(
                        stubComment.author().id(),
                        stubComment.post().id(),
                        stubComment.information().content());

        final var commentId = commentCommandService.write(command);

        assertThat(commentId).isEqualTo(stubComment.id());
    }

    @Test
    @DisplayName("Write comment post not found")
    void write_comment_post_not_found() {
        final var command = new WriteCommand(2L, 5L, "content");
        given(postQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        final ThrowingCallable when = () -> commentCommandService.write(command);

        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("Edit comment")
    void edit_comment() {
        final var stubComment = getStubComment(8L, getStubPost(11L), getStubAuthor(12L));
        given(userQueryPort.findById(stubComment.author().id()))
                .willReturn(Optional.of(stubComment.author()));
        given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));
        final var command =
                new EditCommand(stubComment.author().id(), stubComment.id(), "newContent");

        commentCommandService.edit(command);

        assertThat(stubComment.information().content()).isEqualTo(command.newContent());
    }

    @Test
    @DisplayName("Edit comment not found")
    void edit_comment_not_found() {
        final var command = new EditCommand(51L, 1L, "newContent");
        given(commentQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        final ThrowingCallable when = () -> commentCommandService.edit(command);

        assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    @DisplayName("Edit comment access denied")
    void edit_comment_access_denied() {
        final var stubComment = getStubComment(8L, getStubPost(11L), getStubAuthor(88L));
        given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));

        final var stubAuthor = getStubAuthor(12L);
        given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
        final var command = new EditCommand(stubAuthor.id(), stubComment.id(), "newContent");

        final ThrowingCallable when = () -> commentCommandService.edit(command);

        assertThat(stubComment.author().id()).isNotEqualTo(command.authorId());
        assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
        assertThat(stubComment.information().content()).isNotEqualTo(command.newContent());
    }

    @Test
    @DisplayName("Delete comment")
    void delete_comment() {
        final var stubComment = getStubComment(56L, getStubPost(65L), getStubAuthor(6L));
        given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));
        given(userQueryPort.findById(stubComment.author().id()))
                .willReturn(Optional.of(stubComment.author()));
        final var command = new DeleteCommand(stubComment.author().id(), stubComment.id());

        commentCommandService.delete(command);

        then(commentCommandPort).should().delete(any(Comment.class));
    }

    @Test
    @DisplayName("Delete comment not found")
    void delete_comment_not_found() {
        final var command = new DeleteCommand(5L, 1L);
        given(commentQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        final ThrowingCallable when = () -> commentCommandService.delete(command);

        assertThatThrownBy(when).isInstanceOf(CommentNotFoundException.class);
        then(commentCommandPort).should(never()).delete(any(Comment.class));
    }

    @Test
    @DisplayName("Delete comment access denied")
    void delete_comment_access_denied() {
        final var stubComment = getStubComment(8L, getStubPost(11L), getStubAuthor(88L));
        given(commentQueryPort.findById(stubComment.id())).willReturn(Optional.of(stubComment));

        final var stubAuthor = getStubAuthor(12L);
        given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
        final var command = new DeleteCommand(stubAuthor.id(), stubComment.id());

        final ThrowingCallable when = () -> commentCommandService.delete(command);

        assertThat(stubComment.author().id()).isNotEqualTo(command.authorId());
        assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
        then(commentCommandPort).should(never()).delete(any(Comment.class));
    }

    private Comment getStubComment(final Long commentId, final Post post, final User author) {
        final var comment = CommentFactory.create(post, author);
        ReflectionTestUtils.setField(comment, "id", commentId);
        return comment;
    }

    private Post getStubPost(final Long postId) {
        final var post = PostFactory.create();
        ReflectionTestUtils.setField(post, "id", postId);
        return post;
    }

    private User getStubAuthor(final Long authorId) {
        final var author = UserFactory.create();
        ReflectionTestUtils.setField(author, "id", authorId);
        return author;
    }
}
