package me.jaeyeop.blog.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.command.DeleteUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.response.UserProfile;
import me.jaeyeop.blog.user.adapter.out.UserPersistenceAdapter;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class UserCommandServiceTest {

  private UserRepository userRepository;

  private UserCommandUseCase userCommandUseCase;

  @BeforeEach
  void setUp() {
    userRepository = Mockito.mock(UserRepository.class);
    final var userPersistencePort = new UserPersistenceAdapter(userRepository);
    userCommandUseCase = new UserCommandService(
        userPersistencePort,
        userPersistencePort);
  }

  @Test
  void 프로필_업데이트() {
    final var user = UserFactory.createDefault();
    final var command = new UpdateUserProfileCommand("newName", "newPicture");
    final var expected = UserProfile.from(
        UserFactory.createUpdate(command.getName(), command.getPicture()));
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

    final var actual = userCommandUseCase.updateProfile(user.getEmail(), command);

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void 존재하지_않는_프로필_업데이트() {
    final var user = UserFactory.createDefault();
    final var command = new UpdateUserProfileCommand("newName", "newPicture");
    given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());

    final ThrowingCallable when = () -> userCommandUseCase.updateProfile(user.getEmail(), command);

    assertThatThrownBy(when).isInstanceOf(EmailNotFoundException.class);
  }

  @Test
  void 프로필_삭제() {
    final var user = UserFactory.createDefault();
    final var command = new DeleteUserProfileCommand(user.getEmail());

    userCommandUseCase.deleteProfile(command);

    then(userRepository).should(only()).deleteByEmail(user.getEmail());
  }

}