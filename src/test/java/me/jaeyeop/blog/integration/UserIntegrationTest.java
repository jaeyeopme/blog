package me.jaeyeop.blog.integration;

import static me.jaeyeop.blog.user.adapter.in.UserWebAdapter.USER_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.factory.UserSecurityContextFactory.WithPrincipal;
import me.jaeyeop.blog.user.adapter.in.UpdateUserRequestDto;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class UserIntegrationTest extends IntegrationTest {
    @Autowired
    protected UserRepository userRepository;

    @WithPrincipal
    @Test
    void 자신의_프로필_조회() throws Exception {
        // GIVE
        final var profile = getPrincipalUser().profile();

        // WHEN
        final var when = mockMvc.perform(get(USER_API_URI + "/me"));

        // THEN
        when.andExpectAll(status().isOk(), content().json(toJson(profile)));
    }

    @WithPrincipal
    @Test
    void 이메일로_프로필_조회() throws Exception {
        // GIVE
        final var profile = getPrincipalUser().profile();

        // WHEN
        final var when = mockMvc.perform(get(USER_API_URI + "/{id}", getPrincipalUser().id()));

        // THEN
        when.andExpectAll(status().isOk(), content().json(toJson(profile)));
    }

    @WithPrincipal
    @Test
    void 프로필_업데이트() throws Exception {
        // GIVEN
        final var user = getPrincipalUser();
        final var request = new UpdateUserRequestDto("newName", "newIntroduce");

        // THEN
        final var when =
                mockMvc.perform(
                        patch(USER_API_URI + "/me")
                                .contentType(APPLICATION_JSON)
                                .content(toJson(request)));

        // WHEN
        when.andExpectAll(status().isNoContent());
        final var optionalUpdatedUser = userRepository.findById(user.id());
        assertThat(optionalUpdatedUser).isPresent();

        final var updatedUser = optionalUpdatedUser.get();
        assertThat(updatedUser.profile().name()).isEqualTo(request.name());
        assertThat(updatedUser.profile().introduce()).isEqualTo(request.introduce());
    }

    @WithPrincipal
    @Test
    void 프로필_삭제() throws Exception {
        // GIVEN
        final var user = getPrincipalUser();
        final var post = getPost(user);
        final var comment = getComment(post, user);

        // WHEN
        final var when = mockMvc.perform(delete(USER_API_URI + "/me"));

        // THEN
        when.andExpectAll(status().isNoContent());
        assertThat(userRepository.findById(user.id())).isNotPresent();
        assertThat(postJpaRepository.findById(post.id())).isNotPresent();
        assertThat(commentJpaRepository.findById(comment.id())).isNotPresent();
    }
}
