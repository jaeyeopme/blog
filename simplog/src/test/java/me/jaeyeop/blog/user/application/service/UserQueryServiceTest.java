package me.jaeyeop.blog.user.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import java.util.Optional;
import me.jaeyeop.blog.config.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTestSupport;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Find;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;

class UserQueryServiceTest extends UnitTestSupport {

  @Test
  void 프로필_조회() {
    final var email = "email@email.com";
    final var user1 = UserHelper.create();
    given(userQueryPort.findByEmail(email)).willReturn(Optional.of(user1));
    final var userProfile = Profile.from(user1);

    final var actual = userQueryService.findOneByEmail(new Find(email));

    assertThat(actual).isEqualTo(userProfile);
  }

  @Test
  void 존재하지_않는_프로필_조회() {
    final var email = "anonymous@email.com";
    final var command = new Find(email);
    given(userQueryPort.findByEmail(email)).willReturn(Optional.empty());

    final ThrowingCallable when = () -> userQueryService.findOneByEmail(command);

    assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
  }

}
