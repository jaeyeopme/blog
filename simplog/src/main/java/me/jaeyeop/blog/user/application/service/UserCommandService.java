package me.jaeyeop.blog.user.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Delete;
import me.jaeyeop.blog.user.adapter.in.UserRequest.Update;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
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
      final Update request) {
    final var user = userQueryPort.findByEmail(email)
        .orElseThrow(EmailNotFoundException::new);

    user.updateProfile(request.name(), request.picture());
  }

  @Override
  public void delete(final Delete request) {
    userCommandPort.deleteByEmail(request.email());
  }

}
