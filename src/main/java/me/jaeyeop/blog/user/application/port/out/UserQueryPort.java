package me.jaeyeop.blog.user.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
public interface UserQueryPort {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
