package me.jaeyeop.blog.user.application.service;

import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.UserRequest;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class UserQueryService implements UserQueryUseCase {

  private final UserQueryPort userQueryPort;

  public UserQueryService(final UserQueryPort userQueryPort) {
    this.userQueryPort = userQueryPort;
  }

  @Override
  public Profile findOneByEmail(final UserRequest.Find request) {
    final var user = userQueryPort.findByEmail(request.email())
        .orElseThrow(EmailNotFoundException::new);

    return Profile.from(user);
  }

}
