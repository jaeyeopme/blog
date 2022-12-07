package me.jaeyeop.blog.user.application.service;

import me.jaeyeop.blog.config.error.exception.UserNotFoundException;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Find;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
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
  public Profile findOneByEmail(final Find request) {
    final var user = userQueryPort.findByEmail(request.email())
        .orElseThrow(UserNotFoundException::new);

    return Profile.from(user);
  }

}
