package me.jaeyeop.blog.user.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByProfileEmail(String email);
}
