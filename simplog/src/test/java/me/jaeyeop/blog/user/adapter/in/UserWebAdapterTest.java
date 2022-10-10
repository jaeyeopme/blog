package me.jaeyeop.blog.user.adapter.in;

import static me.jaeyeop.blog.config.error.ErrorCode.EMAIL_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import me.jaeyeop.blog.config.error.ErrorResponse;
import me.jaeyeop.blog.config.security.WithDefaultUser;
import me.jaeyeop.blog.config.support.WebMvcTestSupport;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.application.service.UserCommandService;
import me.jaeyeop.blog.user.application.service.UserQueryService;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@SuppressWarnings("deprecation")
@Import({UserQueryService.class, UserCommandService.class})
@WebMvcTest(UserWebAdapter.class)
class UserWebAdapterTest extends WebMvcTestSupport {

  @Autowired
  private UserRepository userRepository;

  @WithDefaultUser
  @Test
  void 자신의_프로필_조회() throws Exception {
    final var user = UserFactory.createDefault();
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    final var given = get(UserWebAdapter.USER_API_URI);
    final var response = UserProfile.from(user);

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isOk(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().json(toJson(response)));
  }

  @WithDefaultUser
  @Test
  void 이메일로_프로필_조회() throws Exception {
    final var user = UserFactory.createDefault();
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    final var given = get(UserWebAdapter.USER_API_URI + "/{email}", user.getEmail());
    final var response = UserProfile.from(user);

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isOk(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().json(toJson(response)));
  }

  @WithDefaultUser
  @Test
  void 존재하지_않은_이메일로_프로필_조회() throws Exception {
    final var email = "non@email.com";
    given(userRepository.findByEmail(email)).willReturn(Optional.empty());
    final var given = get(UserWebAdapter.USER_API_URI + "/{email}", email);
    final var response = ErrorResponse.of(EMAIL_NOT_FOUND).getBody();

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isNotFound(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().json(toJson(response)));
  }

  @WithDefaultUser
  @Test
  void 프로필_업데이트() throws Exception {
    final var user = UserFactory.createDefault();
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
    final var command = new UpdateProfileCommand("newName", "newPicture");
    final var given = put(UserWebAdapter.USER_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command));
    final var response = UserProfile.from(
        UserFactory.createUpdate(command.getName(), command.getPicture()));

    final var when = mockMvc.perform(given);

    when.andExpectAll(
        status().isOk(),
        content().contentType(APPLICATION_JSON_UTF8),
        content().json(toJson(response)));
  }

  @WithDefaultUser
  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_이름으로_프로필_업데이트(final String name) throws Exception {
    final var command = new UpdateProfileCommand(name, "newPicture");
    final var given = put(UserWebAdapter.USER_API_URI)
        .contentType(APPLICATION_JSON_UTF8)
        .content(toJson(command));

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isBadRequest());
    then(userRepository).should(never()).findByEmail(any());
  }

  @WithDefaultUser
  @Test
  void 프로필_삭제() throws Exception {
    final var user = UserFactory.createDefault();
    final var given = delete(UserWebAdapter.USER_API_URI);

    final var when = mockMvc.perform(given);

    when.andExpectAll(status().isOk());
    then(userRepository).should(only()).deleteByEmail(user.getEmail());
  }

}