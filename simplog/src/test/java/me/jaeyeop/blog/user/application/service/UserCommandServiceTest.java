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
import me.jaeyeop.blog.user.adapter.in.UserRequest.Delete;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Update;
import me.jaeyeop.blog.user.domain.User;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
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
    final var newName = "newName";
    final var newPicture = "newPicture";

    // WHEN
    userCommandService.update(userId, new Update(newName, newPicture));

    // THEN
    assertThat(user.name()).isEqualTo(newName);
    assertThat(user.picture()).isEqualTo(newPicture);
  }

  @NullAndEmptySource
  @ParameterizedTest
  void 비어있는_이름으로_프로필_업데이트(final String newName) {
    // GIVEN
    final var userId = 12L;
    final var user = getUser(userId);
    given(userQueryPort.findById(userId)).willReturn(Optional.of(user));
    final var newPicture = "newPicture";

    // WHEN
    userCommandService.update(userId, new Update(newName, newPicture));

    // THEN
    assertThat(user.name()).isEqualTo(user.name());
    assertThat(user.picture()).isEqualTo(newPicture);
  }

  @Test
  void 존재하지_않은_프로필_업데이트() {
    // GIVEN
    final var userId = 4L;
    given(userQueryPort.findById(userId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> userCommandService.update(
        userId, new Update("newName", "newPicture"));

    // THEN
    assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
  }

  @Test
  void 프로필_삭제() {
    // GIVEN
    final var userId = 66L;
    final var user = getUser(userId);
    given(userQueryPort.findById(userId)).willReturn(Optional.of(user));

    // WHEN
    userCommandService.delete(new Delete(userId));

    // THEN
    then(userCommandPort).should().delete(user);
  }

  @Test
  void 존재하지_않은_프로필_삭제() {
    // GIVEN
    final var userId = 33L;
    given(userQueryPort.findById(userId)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> userCommandService.delete(new Delete(userId));

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
