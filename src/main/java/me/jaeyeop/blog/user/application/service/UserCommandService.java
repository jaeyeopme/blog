package me.jaeyeop.blog.user.application.service;

import jakarta.transaction.Transactional;
import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Service;

/**
 * @author jaeyeopme Created on 09/29/2022.
 */
@Transactional
@Service
public class UserCommandService implements UserCommandUseCase {

    private final UserQueryPort userQueryPort;

    private final UserCommandPort userCommandPort;

    public UserCommandService(
            final UserQueryPort userQueryPort, final UserCommandPort userCommandPort) {
        this.userQueryPort = userQueryPort;
        this.userCommandPort = userCommandPort;
    }

    @Override
    public void update(final UpdateCommand command) {
        final var profile = findById(command.targetId()).profile();
        profile.update(command.newName(), command.newIntroduce());
    }

    @Override
    public void delete(final DeleteCommand command) {
        final var user = findById(command.targetId());
        userCommandPort.delete(user);
    }

    private User findById(final Long id) {
        return userQueryPort.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
