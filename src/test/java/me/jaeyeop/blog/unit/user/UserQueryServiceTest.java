package me.jaeyeop.blog.unit.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.UserFactory;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase.ProfileQuery;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.application.service.UserQueryService;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class UserQueryServiceTest extends UnitTest {
    @InjectMocks private UserQueryService userQueryService;
    @Mock(stubOnly = true)
    private UserQueryPort userQueryPort;

    @Test
    void 프로필_조회() {
        // GIVEN
        final var email = "email@email.com";
        final var user = UserFactory.create();
        given(userQueryPort.findByEmail(email)).willReturn(Optional.of(user));
        final var profile = user.profile();

        // WHEN
        final var actual = userQueryService.findProfileByEmail(new ProfileQuery(email));

        // THEN
        assertThat(actual).isEqualTo(profile);
    }

    @Test
    void 존재하지_않는_프로필_조회() {
        // GIVEN
        final var email = "anonymous@email.com";
        given(userQueryPort.findByEmail(email)).willReturn(Optional.empty());

        // WHEN
        final ThrowingCallable when =
                () -> userQueryService.findProfileByEmail(new ProfileQuery(email));

        // THEN
        assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
    }
}
