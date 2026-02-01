package me.jaeyeop.blog.user.adapter.out;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeop.blog.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByProfileEmail(String email);
}
