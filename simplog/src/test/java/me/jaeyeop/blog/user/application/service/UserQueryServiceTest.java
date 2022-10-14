package me.jaeyeop.blog.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.command.GetUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.response.UserProfile;
import me.jaeyeop.blog.user.adapter.out.UserPersistenceAdapter;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserQueryServiceTest {

  private UserRepository userRepository;

  private UserQueryUseCase userQueryUseCase;

  @BeforeEach
  void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    userQueryUseCase = new UserQueryService(
        new UserPersistenceAdapter(userRepository));
  }

  @Test
  void 프로필_조회() {
    final var email = "email@email.com";
    final var command = new GetUserProfileCommand(email);
    final var user1 = UserFactory.createUser1();
    final var expected = UserProfile.from(user1);
    given(userRepository.findByEmail(email)).willReturn(Optional.of(user1));

    final var actual = userQueryUseCase.getProfile(command);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 존재하지_않는_프로필_조회() {
    final var email = "anonymous@email.com";
    final var command = new GetUserProfileCommand(email);
    given(userRepository.findByEmail(email)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> userQueryUseCase.getProfile(command);

    assertThatThrownBy(when).isInstanceOf(EmailNotFoundException.class);
  }

}