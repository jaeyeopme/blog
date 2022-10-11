package me.jaeyeop.blog.user.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserPersistenceAdapter implements UserCommandPort, UserQueryPort {

  private final UserRepository userRepository;

  public UserPersistenceAdapter(final UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User save(final User user) {
    return userRepository.save(user);
  }

  @Override
  public void deleteByEmail(final String email) {
    userRepository.deleteByEmail(email);
  }

  @Override
  public Optional<User> findByEmail(final String email) {
    return userRepository.findByEmail(email);
  }

}
