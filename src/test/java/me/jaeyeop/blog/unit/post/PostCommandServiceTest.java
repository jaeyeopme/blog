package me.jaeyeop.blog.unit.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.Optional;
import me.jaeyeop.blog.commons.error.exception.AccessDeniedException;
import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.DeleteCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.EditCommand;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase.WriteCommand;
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

class PostCommandServiceTest extends UnitTest {

    @Test
    void 게시글_저장() {
        // GIVEN
        final var stubPost = getStubPost(2L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postCommandPort.create(any())).willReturn(stubPost);
        final var command =
                new WriteCommand(
                        stubPost.author().id(),
                        stubPost.information().title(),
                        stubPost.information().content());

        // WHEN
        final var post = postCommandService.write(command);

        // THEN
        assertThat(post).isEqualTo(stubPost.id());
    }

    @Test
    void 게시글_수정() {
        // GIVEN
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command =
                new EditCommand(stubPost.author().id(), stubPost.id(), "newTitle", "newContent");

        // WHEN
        postCommandService.edit(command);

        // THEN
        assertThat(stubPost.information().title()).isEqualTo(command.newTitle());
        assertThat(stubPost.information().content()).isEqualTo(command.newContent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비어있는_제목으로_게시글_수정(final String newTitle) {
        // GIVEN
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command =
                new EditCommand(stubPost.author().id(), stubPost.id(), newTitle, "newContent");

        // WHEN
        postCommandService.edit(command);

        // THEN
        assertThat(stubPost.information().title()).isNotEqualTo(newTitle);
        assertThat(stubPost.information().content()).isEqualTo(command.newContent());
    }

    @ParameterizedTest
    @NullAndEmptySource
    void 비어있는_내용으로_게시글_수정(final String newContent) {
        // GIVEN
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command =
                new EditCommand(stubPost.author().id(), stubPost.id(), "newTitle", newContent);

        // WHEN
        postCommandService.edit(command);

        // THEN
        assertThat(stubPost.information().title()).isEqualTo(command.newTitle());
        assertThat(stubPost.information().content()).isNotEqualTo(command.newContent());
    }

    @Test
    void 존재하지_않은_게시글_수정() {
        // GIVEN
        final var command = new EditCommand(1L, 1L, "newTitle", "newContent");
        given(postQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        // WHEN
        final ThrowingCallable when = () -> postCommandService.edit(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
    }

    @Test
    void 다른_사람의_게시글_수정() {
        // GIVEN
        final var stubPost = getStubPost(2L);
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));

        final var stubAuthor = getStubAuthor(5L);
        given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
        final var command =
                new EditCommand(stubAuthor.id(), stubPost.id(), "newTitle", "newContent");

        // WHEN
        final ThrowingCallable when = () -> postCommandService.edit(command);

        // THEN
        assertThat(stubPost.author().id()).isNotEqualTo(command.authorId());
        assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
        assertThat(stubPost.information().title()).isNotEqualTo(command.newTitle());
        assertThat(stubPost.information().content()).isNotEqualTo(command.newContent());
    }

    @Test
    void 게시글_삭제() {
        // GIVEN
        final var stubPost = getStubPost(1L);
        given(userQueryPort.findById(stubPost.author().id()))
                .willReturn(Optional.of(stubPost.author()));
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));
        final var command = new DeleteCommand(stubPost.author().id(), stubPost.id());

        // WHEN
        postCommandService.delete(command);

        // THEN
        then(postCommandPort).should().delete(stubPost);
    }

    @Test
    void 존재하지_않은_게시글_삭제() {
        // GIVEN
        final var postId = 1L;
        given(postQueryPort.findById(postId)).willReturn(Optional.empty());
        final var command = new DeleteCommand(2L, postId);

        // WHEN
        final ThrowingCallable when = () -> postCommandService.delete(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(PostNotFoundException.class);
        then(postCommandPort).should(never()).delete(any(Post.class));
    }

    @Test
    void 다른_사람의_게시글_삭제() {
        // GIVEN
        final var stubPost = getStubPost(1L);
        given(postQueryPort.findById(stubPost.id())).willReturn(Optional.of(stubPost));

        final var stubAuthor = getStubAuthor(11L);
        given(userQueryPort.findById(stubAuthor.id())).willReturn(Optional.of(stubAuthor));
        final var command = new DeleteCommand(stubAuthor.id(), stubPost.id());

        // WHEN
        final ThrowingCallable when = () -> postCommandService.delete(command);

        // THEN
        assertThat(stubPost.author().id()).isNotEqualTo(command.authorId());
        assertThatThrownBy(when).isInstanceOf(AccessDeniedException.class);
        then(postCommandPort).should(never()).delete(any(Post.class));
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
