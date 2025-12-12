package me.jaeyeop.blog.user.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByProfileEmail(String email);

}
