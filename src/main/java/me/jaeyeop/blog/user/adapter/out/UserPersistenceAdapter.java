package me.jaeyeop.blog.user.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Component;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
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
    public Optional<User> findById(final Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByEmail(final String email) {
        return userRepository.findByProfileEmail(email);
    }

    @Override
    public void delete(final User user) {
        userRepository.delete(user);
    }
}
