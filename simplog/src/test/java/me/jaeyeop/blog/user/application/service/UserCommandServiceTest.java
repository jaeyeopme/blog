package me.jaeyeop.blog.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.domain.User;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 10/09/2022.
 */
class UserCommandServiceTest extends UnitTest {

  @Test
  void 프로필_업데이트() {
    // GIVEN
    final var userId = 81L;
    final var user = getUser(userId);
    given(userQueryPort.findById(userId)).willReturn(Optional.of(user));
    final var command = new UserCommandUseCase.Update(
        userId, "newName", "newIntroduce");

    // WHEN
    userCommandService.update(command);

    // THEN
    assertThat(user.profile().name()).isEqualTo(command.newName());
    assertThat(user.profile().introduce()).isEqualTo(command.newIntroduce());
  }

  @Test
  void 존재하지_않은_프로필_업데이트() {
    // GIVEN
    final var userId = 4L;
    given(userQueryPort.findById(userId)).willReturn(Optional.empty());
    final var command = new UserCommandUseCase.Update(
        userId, "newName", "newIntroduce");

    // WHEN
    final ThrowingCallable when = () -> userCommandService.update(command);

    // THEN
    assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 프로필_삭제() {
    // GIVEN
    final var userId = 66L;
    final var user = getUser(userId);
    given(userQueryPort.findById(userId)).willReturn(Optional.of(user));
    final var command = new UserCommandUseCase.Delete(userId);

    // WHEN
    userCommandService.delete(command);

    // THEN
    then(userCommandPort).should().delete(user);
  }

  @Test
  void 존재하지_않은_프로필_삭제() {
    // GIVEN
    final var userId = 33L;
    given(userQueryPort.findById(userId)).willReturn(Optional.empty());
    final var command = new UserCommandUseCase.Delete(userId);

    // WHEN
    final ThrowingCallable when = () -> userCommandService.delete(command);

    // THEN
    assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
    then(userCommandPort).should(never()).delete(any());
  }

  private User getUser(final Long userId) {
    final var user = UserHelper.create();
    ReflectionTestUtils.setField(user, "id", userId);
    return user;
  }

}
