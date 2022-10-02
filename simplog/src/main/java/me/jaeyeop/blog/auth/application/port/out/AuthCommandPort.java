package me.jaeyeop.blog.auth.application.port.out;

import me.jaeyeop.blog.auth.domain.ExpiredToken;

public interface AuthCommandPort {

  void save(ExpiredToken expiredToken);

}
