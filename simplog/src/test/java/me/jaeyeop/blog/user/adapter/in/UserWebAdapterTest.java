package me.jaeyeop.blog.user.adapter.in;

import static me.jaeyeop.blog.config.error.ErrorCode.EMAIL_NOT_FOUND;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.only;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.Optional;
import me.jaeyeop.blog.config.error.ErrorResponse;
import me.jaeyeop.blog.config.security.WithUser1;
import me.jaeyeop.blog.support.WebMvcTestSupport;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserCommand;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.adapter.out.response.UserProfile;
import me.jaeyeop.blog.user.application.service.UserCommandService;
import me.jaeyeop.blog.user.application.service.UserQueryService;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;

@Import({UserCommandService.class, UserQueryService.class})
@WebMvcTest(UserWebAdapter.class)
class UserWebAdapterTest extends WebMvcTestSupport {

  @Autowired
  private UserRepository userRepository;

  @WithUser1
  @Test
  void 자신의_프로필_조회() throws Exception {
    final var user = UserFactory.createUser1();
    final var profile = UserProfile.from(user);
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

    final var when = mockMvc.perform(
        get(UserWebAdapter.USER_API_URI));

    when.andExpectAll(
        status().isOk(),
        content().json(toJson(profile)));
  }

  @Test
  void 이메일로_프로필_조회() throws Exception {
    final var email = "email@email.com";
    final var user1 = UserFactory.createUser1(email);
    final var profile = UserProfile.from(user1);
    given(userRepository.findByEmail(email)).willReturn(Optional.of(user1));

    final var when = mockMvc.perform(
        get(UserWebAdapter.USER_API_URI + "/{email}", email));

    when.andExpectAll(
        status().isOk(),
        content().json(toJson(profile)));
  }

  @Test
  void 존재하지_않는_이메일로_프로필_조회() throws Exception {
    final var email = "anonymous@email.com";
    final var error = ErrorResponse.of(EMAIL_NOT_FOUND).getBody();
    given(userRepository.findByEmail(email)).willReturn(Optional.empty());

    final var when = mockMvc.perform(
        get(UserWebAdapter.USER_API_URI + "/{email}", email));

    when.andExpectAll(
        status().isNotFound(),
        content().json(toJson(error)));
  }

  @WithUser1
  @Test
  void 프로필_업데이트() throws Exception {
    final var user1 = UserFactory.createUser1();
    final var command = new UpdateUserCommand("newName", "newPicture");
    given(userRepository.findByEmail(user1.getEmail())).willReturn(Optional.of(user1));

    final var when = mockMvc.perform(
        patch(UserWebAdapter.USER_API_URI)
            .contentType(APPLICATION_JSON)
            .content(toJson(command)));

    when.andExpectAll(status().isOk());
  }

  @WithUser1
  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_이름으로_프로필_업데이트(final String name) throws Exception {
    final var command = new UpdateUserCommand(name, "newPicture");

    final var when = mockMvc.perform(
        patch(UserWebAdapter.USER_API_URI)
            .contentType(APPLICATION_JSON)
            .content(toJson(command)));

    when.andExpectAll(status().isBadRequest());
    then(userRepository).should(never()).findByEmail(any());
  }

  @WithUser1
  @Test
  void 프로필_삭제() throws Exception {
    final var when = mockMvc.perform(
        delete(UserWebAdapter.USER_API_URI));

    when.andExpectAll(status().isOk());
    then(userRepository).should(only()).deleteByEmail(any());
  }

}