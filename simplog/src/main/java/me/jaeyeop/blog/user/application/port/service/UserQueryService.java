package me.jaeyeop.blog.user.application.port.service;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.config.error.exception.EmailNotFoundException;
import me.jaeyeop.blog.user.adapter.in.GetProfileCommand;
import me.jaeyeop.blog.user.application.port.in.UserProfile;
import me.jaeyeop.blog.user.application.port.in.UserQueryUseCase;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class UserQueryService implements UserQueryUseCase {

  private final UserQueryPort userQueryPort;

  @Override
  public UserProfile getProfile(final GetProfileCommand command) {
    final var user = userQueryPort.findByEmail(command.getEmail())
        .orElseThrow(EmailNotFoundException::new);

    return UserProfile.from(user);
  }

}
