package me.jaeyeop.blog.unit.post;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import me.jaeyeop.blog.commons.error.exception.AccessDeniedException;
import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.DeleteCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.EditCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.WriteCommand;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.application.service.PostCommandService;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.UnitTest;
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

class PostCommandServiceTest extends UnitTest {
    @InjectMocks
    private PostCommandService postCommandService;

    @Mock
    private PostCommandPort postCommandPort;

    @Mock(stubOnly = true)
    private PostQueryPort postQueryPort;

    @Mock(stubOnly = true)
    private UserQueryPort userQueryPort;

    @Test
    @DisplayName("Write post")
    void write_post() {
        final var stubPost = getStubPost(2L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postCommandPort.create(any())).willReturn(stubPost);
        final var command =
                new WriteCommand(
                        stubPost.author().id(),
                        stubPost.information().title(),
                        stubPost.information().content());

        final var post = postCommandService.write(command);

        assertThat(post).isEqualTo(stubPost.id());
    }

    @Test
    @DisplayName("Edit post")
    void edit_post() {
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command =
                new EditCommand(stubPost.author().id(), stubPost.id(), "newTitle", "newContent");

        postCommandService.edit(command);

        assertThat(stubPost.information().title()).isEqualTo(command.newTitle());
        assertThat(stubPost.information().content()).isEqualTo(command.newContent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Edit post with empty title")
    void edit_post_with_empty_title(final String newTitle) {
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command =
                new EditCommand(stubPost.author().id(), stubPost.id(), newTitle, "newContent");

        postCommandService.edit(command);

        assertThat(stubPost.information().title()).isNotEqualTo(newTitle);
        assertThat(stubPost.information().content()).isEqualTo(command.newContent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @DisplayName("Edit post with empty content")
    void edit_post_with_empty_content(final String newContent) {
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command =
                new EditCommand(stubPost.author().id(), stubPost.id(), "newTitle", newContent);

        postCommandService.edit(command);

        assertThat(stubPost.information().title()).isEqualTo(command.newTitle());
        assertThat(stubPost.information().content()).isNotEqualTo(command.newContent());
    }

    @Test
    @DisplayName("Edit post not found")
    void edit_post_not_found() {
        final var command = new EditCommand(1L, 1L, "newTitle", "newContent");
        given(postQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        final ThrowingCallable when = () -> postCommandService.edit(command);

        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    @DisplayName("Edit post access denied")
    void edit_post_access_denied() {
        final var stubPost = getStubPost(2L);
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));

        final var stubAuthor = getStubAuthor(5L);
        given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
        final var command =
                new EditCommand(stubAuthor.id(), stubPost.id(), "newTitle", "newContent");

        final ThrowingCallable when = () -> postCommandService.edit(command);

        assertThat(stubPost.author().id()).isNotEqualTo(command.authorId());
        assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
        assertThat(stubPost.information().title()).isNotEqualTo(command.newTitle());
        assertThat(stubPost.information().content()).isNotEqualTo(command.newContent());
    }

    @Test
    @DisplayName("Delete post")
    void delete_post() {
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command = new DeleteCommand(stubPost.author().id(), stubPost.id());

        postCommandService.delete(command);

        then(postCommandPort).should().delete(stubPost);
    }

    @Test
    @DisplayName("Delete post not found")
    void delete_post_not_found() {
        final var postId = 1L;
        given(postQueryPort.findById(postId)).willReturn(Optional.empty());
        final var command = new DeleteCommand(2L, postId);

        final ThrowingCallable when = () -> postCommandService.delete(command);

        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
        then(postCommandPort).should(never()).delete(any(Post.class));
    }

    @Test
    @DisplayName("Delete post access denied")
    void delete_post_access_denied() {
        final var stubPost = getStubPost(1L);
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));

        final var stubAuthor = getStubAuthor(11L);
        given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
        final var command = new DeleteCommand(stubAuthor.id(), stubPost.id());

        final ThrowingCallable when = () -> postCommandService.delete(command);

        assertThat(stubPost.author().id()).isNotEqualTo(command.authorId());
        assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
        then(postCommandPort).should(never()).delete(any(Post.class));
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
