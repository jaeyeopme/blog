package me.jaeyeop.blog.user.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import me.jaeyeop.blog.user.domain.UserProfile;

@Transactional(readOnly = true)
@Service
public class UserQueryService implements UserQueryUseCase {
    private final UserQueryPort userQueryPort;

    public UserQueryService(final UserQueryPort userQueryPort) {
        this.userQueryPort = userQueryPort;
    }

    @Override
    public UserProfile findProfileById(final ProfileQuery profileQuery) {
        return findById(profileQuery.id()).profile();
    }

    private User findById(final Long id) {
        return userQueryPort.findById(id).orElseThrow(UserNotFoundException::new);
    }
}
