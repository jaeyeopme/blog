package me.jaeyeop.blog.integration;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import me.jaeyeop.blog.comment.adapter.in.EditCommentRequestDto;
import me.jaeyeop.blog.comment.adapter.in.WriteCommentRequestDto;
import me.jaeyeop.blog.comment.adapter.out.CommentInformationProjectionDto;
import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.factory.UserSecurityContextFactory.WithPrincipal;

import static me.jaeyeop.blog.comment.adapter.in.CommentWebAdapter.COMMENT_API_URI;
import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("OptionalGetWithoutIsPresent")
class CommentIntegrationTest extends IntegrationTest {
    @Test
    @WithPrincipal
    @DisplayName("Write comment")
    void write_comment() throws Exception {
        final var post = getPost(getPrincipalUser());
        final var request = new WriteCommentRequestDto("content");

        final var when =
                mockMvc.perform(
                        post(POST_API_URI + "/{postId}/comments", post.id())
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request)));

        when.andExpectAll(status().isCreated());
    }

    @WithPrincipal
    @NullAndEmptySource
    @ParameterizedTest
    @DisplayName("Write empty comment")
    void write_empty_comment(final String content) throws Exception {
        final var post = getPost(getPrincipalUser());
        final var request = new WriteCommentRequestDto(content);

        final var when =
                mockMvc.perform(
                        post(POST_API_URI + "/{postId}/comments", post.id())
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request)));

        when.andExpectAll(status().isBadRequest());
    }

    @Test
    @WithPrincipal
    @DisplayName("Get comment")
    void get_comment() throws Exception {
        final var comment = getComment(getPost(getPrincipalUser()), getPrincipalUser());
        final var information =
                new CommentInformationProjectionDto(
                        comment.id(),
                        comment.author().profile().name(),
                        comment.information(),
                        comment.createdAt(),
                        comment.lastModifiedAt());

        final var when =
                mockMvc.perform(
                        get(COMMENT_API_URI + "/{commentId}", comment.id())
                                .contentType(APPLICATION_JSON));

        when.andExpectAll(status().isOk(), content().json(toJson(information)));
    }

    @Test
    @WithPrincipal
    @DisplayName("Get comment page")
    void get_comment_page() throws Exception {
        final var comment = getComment(getPost(getPrincipalUser()), getPrincipalUser());
        final var info =
                new CommentInformationProjectionDto(
                        comment.id(),
                        comment.author().profile().name(),
                        comment.information(),
                        comment.createdAt(),
                        comment.lastModifiedAt());
        final var pageable = PageRequest.of(0, 2);
        final var informationPage = new PageImpl<>(List.of(info), pageable, 1);

        final var when =
                mockMvc.perform(
                        get(
                                        POST_API_URI + "/{postId}/comments?page={page}&size={size}",
                                        comment.post().id(),
                                        pageable.getPageNumber(),
                                        pageable.getPageSize())
                                .contentType(APPLICATION_JSON));

        when.andExpectAll(status().isOk(), content().json(toJson(informationPage)));
    }

    @Test
    @WithPrincipal
    @DisplayName("Update comment")
    void update_comment() throws Exception {
        final var comment = getComment(getPost(getPrincipalUser()), getPrincipalUser());
        final var request = new EditCommentRequestDto("newContent");

        final var when =
                mockMvc.perform(
                        patch(COMMENT_API_URI + "/{commentId}", comment.id())
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request)));

        when.andExpectAll(status().isNoContent());
        assertThat(commentJpaRepository.findById(comment.id()).get().information().content())
                .isEqualTo(request.content());
    }

    @Test
    @WithPrincipal
    @DisplayName("Delete comment")
    void delete_comment() throws Exception {
        final var comment = getComment(getPost(getPrincipalUser()), getPrincipalUser());

        final var when =
                mockMvc.perform(
                        delete(COMMENT_API_URI + "/{commentId}", comment.id())
                                .contentType(APPLICATION_JSON));

        when.andExpectAll(status().isNoContent());
        assertThat(commentJpaRepository.findById(comment.id())).isNotPresent();
    }
}
