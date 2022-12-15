package me.jaeyeop.blog.user.application.service;

import me.jaeyeop.blog.config.error.exception.UserNotFoundException;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import me.jaeyeop.blog.user.domain.UserProfile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
@Transactional(readOnly = true)
@Service
public class UserQueryService implements UserQueryUseCase {

  private final UserQueryPort userQueryPort;

  public UserQueryService(final UserQueryPort userQueryPort) {
    this.userQueryPort = userQueryPort;
  }

  @Override
  public UserProfile findProfileByEmail(final ProfileQuery profileQuery) {
    return findByEmail(profileQuery.email()).profile();
  }

  private User findByEmail(final String email) {
    return userQueryPort.findByEmail(email)
        .orElseThrow(UserNotFoundException::new);
  }

}
