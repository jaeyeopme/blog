package me.jaeyeop.blog.user.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.command.DeleteUserCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserCommand;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class UserCommandService implements UserCommandUseCase {

  private final UserQueryPort userQueryPort;

  private final UserCommandPort userCommandPort;

  public UserCommandService(final UserQueryPort userQueryPort,
      final UserCommandPort userCommandPort) {
    this.userQueryPort = userQueryPort;
    this.userCommandPort = userCommandPort;
  }

  @Override
  public void update(final String email,
      final UpdateUserCommand command) {
    final User user = userQueryPort.findByEmail(email)
        .orElseThrow(EmailNotFoundException::new);

    user.updateProfile(command.getName(), command.getPicture());
  }

  @Override
  public void delete(final DeleteUserCommand command) {
    userCommandPort.deleteByEmail(command.getEmail());
  }

}
