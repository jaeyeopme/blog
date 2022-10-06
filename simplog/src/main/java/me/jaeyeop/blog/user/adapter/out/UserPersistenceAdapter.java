package me.jaeyeop.blog.user.adapter.out;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPersistenceAdapter implements UserCommandPort, UserQueryPort {

  private final UserRepository userRepository;

  @Override
  public User save(final User user) {
    return userRepository.save(user);
  }

  @Override
  public Optional<User> findByEmail(final String email) {
    return userRepository.findByEmail(email);
  }

}
