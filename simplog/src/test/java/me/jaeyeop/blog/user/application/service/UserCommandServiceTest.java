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
    final var email = "email@email.com";
    final var command = new UpdateUserProfileCommand("newName", "newPicture");
    final var user1 = UserFactory.createUser1();
    given(userRepository.findByEmail(email)).willReturn(Optional.of(user1));

    userCommandUseCase.update(email, command);

    assertThat(user1.getName()).isEqualTo(command.getName());
    assertThat(user1.getPicture()).isEqualTo(command.getPicture());
  }

  @Test
  void 존재하지_않는_프로필_업데이트() {
    final var email = "anonymous@email.com";
    final var command = new UpdateUserProfileCommand("newName", "newPicture");
    given(userRepository.findByEmail(email)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> userCommandUseCase.update(email, command);

    assertThatThrownBy(when).isInstanceOf(EmailNotFoundException.class);
  }

  @Test
  void 프로필_삭제() {
    final var email = "email@email.com";
    final var command = new DeleteUserProfileCommand(email);

    userCommandUseCase.delete(command);

    then(userRepository).should(only()).deleteByEmail(email);
  }

}