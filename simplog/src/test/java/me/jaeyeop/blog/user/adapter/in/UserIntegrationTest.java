package me.jaeyeop.blog.user.adapter.in;

import static me.jaeyeop.blog.user.adapter.in.UserWebAdapter.USER_API_URI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.helper.UserHelper.WithPrincipal;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Update;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jaeyeopme Created on 12/02/2022.
 */
class UserIntegrationTest extends IntegrationTest {

  @Autowired
  private UserRepository userRepository;

  @WithPrincipal
  @Test
  void 자신의_프로필_조회() throws Exception {
    // GIVE
    final var user = getPrincipal();
    final var profile = Profile.from(user);

    // WHEN
    final var when = mockMvc.perform(get(USER_API_URI));

    // THEN
    when.andExpectAll(
        status().isOk(),
        content().json(toJson(profile)));
  }

  @WithPrincipal
  @Test
  void 이메일로_프로필_조회() throws Exception {
    // GIVE
    final var user = getPrincipal();
    final var profile = Profile.from(user);

    // WHEN
    final var when = mockMvc.perform(get(USER_API_URI + "/{email}", user.email()));

    // THEN
    when.andExpectAll(
        status().isOk(),
        content().json(toJson(profile)));
  }

  @WithPrincipal
  @Test
  void 프로필_업데이트() throws Exception {
    // GIVEN
    final var user = getPrincipal();
    final var command = new Update("newName", "newPicture");

    // THEN
    final var when = mockMvc.perform(patch(USER_API_URI)
        .contentType(APPLICATION_JSON)
        .content(toJson(command)));

    // WHEN
    when.andExpectAll(status().isNoContent());
    final var updatedUser = userRepository.findById(user.id()).get();
    assertThat(updatedUser.name()).isEqualTo(command.name());
    assertThat(updatedUser.picture()).isEqualTo(command.picture());
  }

  @WithPrincipal
  @Test
  void 프로필_삭제() throws Exception {
    // GIVEN
    final var user = getPrincipal();

    // WHEN
    final var when = mockMvc.perform(delete(USER_API_URI));

    // THEN
    when.andExpectAll(status().isNoContent());
    final var optional = userRepository.findById(user.id());
    assertThat(optional).isNotPresent();
  }

}
