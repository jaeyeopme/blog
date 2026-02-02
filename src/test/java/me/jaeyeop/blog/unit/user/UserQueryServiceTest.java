package me.jaeyeop.blog.unit.user;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.UserFactory;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase.ProfileQuery;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.application.service.UserQueryService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class UserQueryServiceTest extends UnitTest {
    @InjectMocks
    private UserQueryService userQueryService;

    @Mock(stubOnly = true)
    private UserQueryPort userQueryPort;

    @Test
    @DisplayName("Find profile by id")
    void find_profile_by_id() {
        final var id = 44L;
        final var user = UserFactory.create();
        given(userQueryPort.findById(id)).willReturn(Optional.of(user));
        final var profile = user.profile();

        final var actual = userQueryService.findProfileById(new ProfileQuery(id));

        assertThat(actual).isEqualTo(profile);
    }

    @Test
    @DisplayName("Find profile by id not found")
    void find_profile_by_id_not_found() {
        final var id = 55L;
        given(userQueryPort.findById(id)).willReturn(Optional.empty());

        final ThrowingCallable when = () -> userQueryService.findProfileById(new ProfileQuery(id));

        assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
    }
}
