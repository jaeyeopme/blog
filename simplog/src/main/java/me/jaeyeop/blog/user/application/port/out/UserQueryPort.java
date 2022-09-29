package me.jaeyeop.blog.user.application.port.out;

import me.jaeyeop.blog.user.domain.User;

public interface UserQueryPort {

  boolean existsByEmail(String email);

  User findByEmail(String email);

}
