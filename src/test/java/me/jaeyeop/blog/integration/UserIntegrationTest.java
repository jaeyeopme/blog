package me.jaeyeop.blog.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.factory.UserSecurityContextFactory.WithPrincipal;
import me.jaeyeop.blog.user.adapter.in.UpdateUserRequestDto;
import me.jaeyeop.blog.user.adapter.out.UserRepository;

import static me.jaeyeop.blog.user.adapter.in.UserWebAdapter.USER_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserIntegrationTest extends IntegrationTest {
    @Autowired
    protected UserRepository userRepository;

    @Test
    @WithPrincipal
    @DisplayName("Get my profile")
    void get_my_profile() throws Exception {
        final var profile = getPrincipalUser().profile();

        final var when = mockMvc.perform(get(USER_API_URI + "/me"));

        when.andExpectAll(status().isOk(), content().json(toJson(profile)));
    }

    @Test
    @WithPrincipal
    @DisplayName("Get profile by id")
    void get_profile_by_id() throws Exception {
        final var profile = getPrincipalUser().profile();

        final var when = mockMvc.perform(get(USER_API_URI + "/{id}", getPrincipalUser().id()));

        when.andExpectAll(status().isOk(), content().json(toJson(profile)));
    }

    @Test
    @WithPrincipal
    @DisplayName("Update profile")
    void update_profile() throws Exception {
        final var user = getPrincipalUser();
        final var request = new UpdateUserRequestDto("newName", "newIntroduce");

        final var when =
                mockMvc.perform(
                        patch(USER_API_URI + "/me")
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request)));

        when.andExpectAll(status().isNoContent());
        final var optionalUpdatedUser = userRepository.findById(user.id());
        assertThat(optionalUpdatedUser).isPresent();

        final var updatedUser = optionalUpdatedUser.get();
        assertThat(updatedUser.profile().name()).isEqualTo(request.name());
        assertThat(updatedUser.profile().introduce()).isEqualTo(request.introduce());
    }

    @Test
    @WithPrincipal
    @DisplayName("Delete profile")
    void delete_profile() throws Exception {
        final var user = getPrincipalUser();
        final var post = getPost(user);
        final var comment = getComment(post, user);

        final var when = mockMvc.perform(delete(USER_API_URI + "/me"));

        when.andExpectAll(status().isNoContent());
        assertThat(userRepository.findById(user.id())).isNotPresent();
        assertThat(postJpaRepository.findById(post.id())).isNotPresent();
        assertThat(commentJpaRepository.findById(comment.id())).isNotPresent();
    }
}
