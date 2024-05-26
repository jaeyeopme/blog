package me.jaeyeop.blog.user.application.port.out;

import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 09/29/2022.
 */
public interface UserCommandPort {

  User save(User user);

  void delete(User user);

}
