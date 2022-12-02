package me.jaeyeop.blog.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.only;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTestSupport;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Delete;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Update;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class UserCommandServiceTest extends UnitTestSupport {

  @Test
  void 프로필_업데이트() {
    final var email = "email@email.com";
    final var user = UserHelper.create();
    given(userQueryPort.findByEmail(email)).willReturn(Optional.of(user));
    final var newName = "newName";
    final var newPicture = "newPicture";

    final ThrowingCallable when = () -> userCommandService.update(
        email, new Update(newName, newPicture));

    assertThatNoException().isThrownBy(when);
    assertThat(user.name()).isEqualTo(newName);
    assertThat(user.picture()).isEqualTo(newPicture);
  }

  @Test
  void 존재하지_않은_프로필_업데이트() {
    final var email = "anonymous@email.com";
    given(userQueryPort.findByEmail(email)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> userCommandService.update(
        email, new Update("newName", "newPicture"));

    assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 프로필_삭제() {
    final var email = "email@email.com";

    final ThrowingCallable when = () -> userCommandService.delete(new Delete(email));

    assertThatNoException().isThrownBy(when);
    then(userCommandPort).should(only()).deleteByEmail(email);
  }

}
