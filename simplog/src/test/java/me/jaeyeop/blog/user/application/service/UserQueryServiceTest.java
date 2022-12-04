package me.jaeyeop.blog.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Find;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

/**
 * @author jaeyeopme Created on 10/09/2022.
 */
class UserQueryServiceTest extends UnitTest {

  @Test
  void 프로필_조회() {
    // GIVEN
    final var email = "email@email.com";
    final var user = UserHelper.create();
    given(userQueryPort.findByEmail(email)).willReturn(Optional.of(user));
    final var profile = Profile.from(user);

    // WHEN
    final var actual = userQueryService.findOneByEmail(new Find(email));

    // THEN
    assertThat(actual).isEqualTo(profile);
  }

  @Test
  void 존재하지_않는_프로필_조회() {
    // GIVEN
    final var email = "anonymous@email.com";
    final var command = new Find(email);
    given(userQueryPort.findByEmail(email)).willReturn(Optional.empty());

    // WHEN
    final ThrowingCallable when = () -> userQueryService.findOneByEmail(command);

    // THEN
    assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
  }

}
