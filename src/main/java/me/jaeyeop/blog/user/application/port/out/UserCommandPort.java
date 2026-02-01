package me.jaeyeop.blog.user.application.port.out;

import me.jaeyeop.blog.user.domain.User;

public interface UserCommandPort {
    User save(User user);

    void delete(User user);
}
