package me.jaeyeop.blog.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Delete;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Update;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.UserFactory;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceTest {

  @Mock
  private UserCommandPort userCommandPort;

  @Mock(stubOnly = true)
  private UserQueryPort userQueryPort;

  @InjectMocks
  private UserCommandService userCommandService;

  @Test
  void 프로필_업데이트() {
    final var email = "email@email.com";
    final var user1 = UserFactory.createUser1();
    given(userQueryPort.findByEmail(email)).willReturn(Optional.of(user1));
    final var newName = "newName";
    final var newPicture = "newPicture";

    final ThrowingCallable when = () -> userCommandService.update(email,
        new Update(newName, newPicture));

    assertThatNoException().isThrownBy(when);
    assertThat(user1.name()).isEqualTo(newName);
    assertThat(user1.picture()).isEqualTo(newPicture);
  }

  @Test
  void 존재하지_않는_프로필_업데이트() {
    final var email = "anonymous@email.com";
    given(userQueryPort.findByEmail(email)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> userCommandService.update(email,
        new Update("newName", "newPicture"));

    assertThatThrownBy(when).isInstanceOf(EmailNotFoundException.class);
  }

  @Test
  void 프로필_삭제() {
    final var email = "email@email.com";

    final ThrowingCallable when = () -> userCommandService.delete(new Delete(email));

    assertThatNoException().isThrownBy(when);
    then(userCommandPort).should(only()).deleteByEmail(email);
  }

}