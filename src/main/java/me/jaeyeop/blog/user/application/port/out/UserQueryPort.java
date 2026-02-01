package me.jaeyeop.blog.user.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.user.domain.User;

public interface UserQueryPort {
    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
