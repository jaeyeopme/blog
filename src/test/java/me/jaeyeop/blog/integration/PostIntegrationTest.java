package me.jaeyeop.blog.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import me.jaeyeop.blog.post.adapter.in.EditPostRequestDto;
import me.jaeyeop.blog.post.adapter.in.WritePostRequestDto;
import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;
import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.factory.UserFactory;
import me.jaeyeop.blog.support.factory.UserSecurityContextFactory.WithPrincipal;

import static me.jaeyeop.blog.post.adapter.in.PostWebAdapter.POST_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class PostIntegrationTest extends IntegrationTest {
    @Test
    @WithPrincipal
    @DisplayName("Write post")
    void write_post() throws Exception {
        final var request = new WritePostRequestDto("title", "content");

        final var when =
                mockMvc.perform(
                        post(POST_API_URI).contentType(APPLICATION_JSON).content(toJson(request)));

        when.andExpectAll(status().isCreated());
    }

    @Test
    @DisplayName("Write post fail unauthorized")
    void write_post_fail_unauthorized() throws Exception {
        final var request = new WritePostRequestDto("title", "content");

        final var when =
                mockMvc.perform(
                        post(POST_API_URI).contentType(APPLICATION_JSON).content(toJson(request)));

        when.andExpectAll(status().isUnauthorized());
    }

    @Test
    @WithPrincipal
    @DisplayName("Get post")
    void get_post() throws Exception {
        final var post = getPost(getPrincipalUser());
        final var information =
                new PostInformationProjectionDto(
                        post.id(),
                        post.information(),
                        post.author().profile().name(),
                        post.createdAt(),
                        post.lastModifiedAt());

        final var when = mockMvc.perform(get(POST_API_URI + "/{id}", post.id()));

        when.andExpectAll(status().isOk(), content().json(toJson(information)));
    }

    @Test
    @DisplayName("Get post success unauthorized")
    void get_post_success_unauthorized() throws Exception {
        final var author = userRepository.save(UserFactory.create());
        final var post = getPost(author);
        final var information =
                new PostInformationProjectionDto(
                        post.id(),
                        post.information(),
                        post.author().profile().name(),
                        post.createdAt(),
                        post.lastModifiedAt());

        final var when = mockMvc.perform(get(POST_API_URI + "/{id}", post.id()));

        when.andExpectAll(status().isOk(), content().json(toJson(information)));
    }

    @Test
    @WithPrincipal
    @DisplayName("Update post")
    void update_post() throws Exception {
        final var savedPost = getPost(getPrincipalUser());
        final var request = new EditPostRequestDto("newTitle", "newContent");

        final var when =
                mockMvc.perform(
                        patch(POST_API_URI + "/{id}", savedPost.id())
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request)));

        when.andExpectAll(status().isNoContent());
        final var post = postJpaRepository.findById(savedPost.id()).get();
        assertThat(post.id()).isEqualTo(savedPost.id());
        assertThat(post.information().title()).isEqualTo(request.title());
        assertThat(post.information().content()).isEqualTo(request.content());
    }

    @Test
    @WithPrincipal
    @DisplayName("Delete post")
    void delete_post() throws Exception {
        final var author = getPrincipalUser();
        final var post = getPost(author);
        final var comment = getComment(post, author);

        final var when = mockMvc.perform(delete(POST_API_URI + "/{id}", post.id()));

        when.andExpectAll(status().isNoContent());
        assertThat(postJpaRepository.findById(post.id())).isNotPresent();
        assertThat(commentJpaRepository.findById(comment.id())).isNotPresent();
    }
}
