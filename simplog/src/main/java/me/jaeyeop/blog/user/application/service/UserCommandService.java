package me.jaeyeop.blog.user.application.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.DeleteProfileCommand;
import me.jaeyeop.blog.user.adapter.in.UpdateProfileCommand;
import me.jaeyeop.blog.user.adapter.in.UserProfile;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class UserCommandService implements UserCommandUseCase {

  private final UserQueryPort userQueryPort;

  private final UserCommandPort userCommandPort;

  @Override
  public UserProfile updateProfile(
      final String email, final UpdateProfileCommand command) {
    final User user = userQueryPort.findByEmail(email)
        .orElseThrow(EmailNotFoundException::new);

    user.updateProfile(command.getName(), command.getPicture());

    return UserProfile.from(user);
  }

  @Override
  public void deleteProfile(final DeleteProfileCommand command) {
    userCommandPort.deleteByEmail(command.getEmail());
  }

}
