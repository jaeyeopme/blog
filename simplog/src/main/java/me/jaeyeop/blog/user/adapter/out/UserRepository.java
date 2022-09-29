package me.jaeyeop.blog.user.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

  boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

}
