package me.jaeyeop.blog.user.application.service;

import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.command.GetUserCommand;
import me.jaeyeop.blog.user.adapter.out.response.UserProfile;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
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
  public UserProfile getOneByEmail(final GetUserCommand command) {
    final User user = userQueryPort.findByEmail(command.getEmail())
        .orElseThrow(EmailNotFoundException::new);

    return UserProfile.from(user);
  }

}
