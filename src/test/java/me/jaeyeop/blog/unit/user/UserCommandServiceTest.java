package me.jaeyeop.blog.unit.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

import java.util.Optional;
import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.UserFactory;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase.DeleteCommand;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase.UpdateCommand;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.application.service.UserCommandService;
import me.jaeyeop.blog.user.domain.User;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

class UserCommandServiceTest extends UnitTest {
    @InjectMocks private UserCommandService userCommandService;
    @Mock private UserCommandPort userCommandPort;

    @Mock(stubOnly = true)
    private UserQueryPort userQueryPort;

    @Test
    void 프로필_업데이트() {
        // GIVEN
        final var user = getUser(81L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new UpdateCommand(user.id(), "newName", "newIntroduce");

        // WHEN
        userCommandService.update(command);

        // THEN
        assertThat(user.profile().name()).isEqualTo(command.newName());
        assertThat(user.profile().introduce()).isEqualTo(command.newIntroduce());
    }

    @NullAndEmptySource
    @ParameterizedTest
    void 비어있는_이름으로_프로필_업데이트(final String newName) {
        // GIVEN
        final var user = getUser(81L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new UpdateCommand(user.id(), newName, "newIntroduce");

        // WHEN
        userCommandService.update(command);

        // THEN
        assertThat(user.profile().name()).isNotEqualTo(command.newName());
        assertThat(user.profile().introduce()).isEqualTo(command.newIntroduce());
    }

    @NullAndEmptySource
    @ParameterizedTest
    void 비어있는_소개로_프로필_업데이트(final String newIntroduce) {
        // GIVEN
        final var user = getUser(81L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new UpdateCommand(user.id(), "newName", newIntroduce);

        // WHEN
        userCommandService.update(command);

        // THEN
        assertThat(user.profile().name()).isEqualTo(command.newName());
        assertThat(user.profile().introduce()).isNotEqualTo(command.newIntroduce());
    }

    @Test
    void 존재하지_않은_프로필_업데이트() {
        // GIVEN
        final var command = new UpdateCommand(4L, "newName", "newIntroduce");
        given(userQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        // WHEN
        final ThrowingCallable when = () -> userCommandService.update(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void 프로필_삭제() {
        // GIVEN
        final var user = getUser(66L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new DeleteCommand(user.id());

        // WHEN
        userCommandService.delete(command);

        // THEN
        then(userCommandPort).should().delete(user);
    }

    @Test
    void 존재하지_않은_프로필_삭제() {
        // GIVEN
        final var command = new DeleteCommand(33L);
        given(userQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        // WHEN
        final ThrowingCallable when = () -> userCommandService.delete(command);

        // THEN
        assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
        then(userCommandPort).should(never()).delete(any(User.class));
    }

    private User getUser(final Long userId) {
        final var user = UserFactory.create();
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }
}
