package me.jaeyeop.blog.user.adapter.in;

import static me.jaeyeop.blog.config.error.ErrorCode.EMAIL_NOT_FOUND;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import me.jaeyeop.blog.config.error.ErrorResponse;
import me.jaeyeop.blog.config.security.WithOAuth2User;
import me.jaeyeop.blog.config.support.WebMvcTestSupport;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.application.port.service.UserCommandService;
import me.jaeyeop.blog.user.application.port.service.UserQueryService;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("deprecation")
@Import({UserQueryService.class, UserCommandService.class})
@WebMvcTest(UserWebAdapter.class)
class UserWebAdapterTest extends WebMvcTestSupport {

  @Autowired
  private UserRepository userRepository;

  @WithOAuth2User
  @Test
  void 자신의_프로필_조회() throws Exception {
    final var user = UserFactory.createDefault();
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    final var profile = UserProfile.from(user);
    final var given = get(UserWebAdapter.USER_API_URI);

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isOk(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().json(toJson(profile)));
  }

  @WithOAuth2User
  @Test
  void 이메일로_프로필_조회() throws Exception {
    final var user = UserFactory.createDefault();
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    final var given = get(UserWebAdapter.USER_API_URI + "/{email}", user.getEmail());

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isOk(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().string(toJson(UserProfile.from(user))));
  }

  @WithOAuth2User
  @Test
  void 존재하지_않은_이메일로_프로필_조회() throws Exception {
    given(userRepository.findByEmail("non@email.com")).willReturn(Optional.empty());
    final var given = get(UserWebAdapter.USER_API_URI + "/{email}", "non@email.com");

    final var when = mockMvc.perform(given);

    final var error = ErrorResponse.of(EMAIL_NOT_FOUND).getBody();
    when.andExpectAll(
        status().isNotFound(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().string(toJson(error)));
  }

  @WithOAuth2User
  @Test
  void 프로필_업데이트() throws Exception {
    final var user = UserFactory.createDefault();
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    final var command = new UpdateProfileCommand("newName", "newPicture");
    final var given = put(UserWebAdapter.USER_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command));
    ReflectionTestUtils.setField(user, "name", command.getName());
    ReflectionTestUtils.setField(user, "picture", command.getPicture());

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isOk(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().string(toJson(UserProfile.from(user))));
  }

  @WithOAuth2User
  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_이름으로_프로필_업데이트(final String name) throws Exception {
    final var command = new UpdateProfileCommand(name, "newPicture");
    final var given = put(UserWebAdapter.USER_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command));

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isBadRequest());
  }

}