package me.jaeyeop.blog.user.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.command.DeleteUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.response.UserProfile;
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
  public UserProfile updateProfile(
      final String email, final UpdateUserProfileCommand command) {
    final User user = userQueryPort.findByEmail(email)
        .orElseThrow(EmailNotFoundException::new);

    user.updateProfile(command.getName(), command.getPicture());

    return UserProfile.from(user);
  }

  @Override
  public void deleteProfile(final DeleteUserProfileCommand command) {
    userCommandPort.deleteByEmail(command.getEmail());
  }

}
