package me.jaeyeop.blog.unit.user;

import java.util.Optional;

import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;

import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.support.UnitTest;
import me.jaeyeop.blog.support.factory.UserFactory;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase.DeleteCommand;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase.UpdateCommand;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.application.service.UserCommandService;
import me.jaeyeop.blog.user.domain.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;

class UserCommandServiceTest extends UnitTest {
    @InjectMocks
    private UserCommandService userCommandService;

    @Mock
    private UserCommandPort userCommandPort;

    @Mock(stubOnly = true)
    private UserQueryPort userQueryPort;

    @Test
    @DisplayName("Update profile")
    void update_profile() {
        final var user = getUser(81L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new UpdateCommand(user.id(), "newName", "newIntroduce");

        userCommandService.update(command);

        assertThat(user.profile().name()).isEqualTo(command.newName());
        assertThat(user.profile().introduce()).isEqualTo(command.newIntroduce());
    }

    @NullAndEmptySource
    @ParameterizedTest
    @DisplayName("Update profile with empty name")
    void update_profile_with_empty_name(final String newName) {
        final var user = getUser(81L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new UpdateCommand(user.id(), newName, "newIntroduce");

        userCommandService.update(command);

        assertThat(user.profile().name()).isNotEqualTo(command.newName());
        assertThat(user.profile().introduce()).isEqualTo(command.newIntroduce());
    }

    @NullAndEmptySource
    @ParameterizedTest
    @DisplayName("Update profile with empty introduce")
    void update_profile_with_empty_introduce(final String newIntroduce) {
        final var user = getUser(81L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new UpdateCommand(user.id(), "newName", newIntroduce);

        userCommandService.update(command);

        assertThat(user.profile().name()).isEqualTo(command.newName());
        assertThat(user.profile().introduce()).isNotEqualTo(command.newIntroduce());
    }

    @Test
    @DisplayName("Update profile not found")
    void update_profile_not_found() {
        final var command = new UpdateCommand(4L, "newName", "newIntroduce");
        given(userQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        final ThrowingCallable when = () -> userCommandService.update(command);

        assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Delete profile")
    void delete_profile() {
        final var user = getUser(66L);
        given(userQueryPort.findById(user.id())).willReturn(Optional.of(user));
        final var command = new DeleteCommand(user.id());

        userCommandService.delete(command);

        then(userCommandPort).should().delete(user);
    }

    @Test
    @DisplayName("Delete profile not found")
    void delete_profile_not_found() {
        final var command = new DeleteCommand(33L);
        given(userQueryPort.findById(command.targetId())).willReturn(Optional.empty());

        final ThrowingCallable when = () -> userCommandService.delete(command);

        assertThatThrownBy(when).isInstanceOf(UserNotFoundException.class);
        then(userCommandPort).should(never()).delete(any(User.class));
    }

    private User getUser(final Long userId) {
        final var user = UserFactory.create();
        ReflectionTestUtils.setField(user, "id", userId);
        return user;
    }
}
