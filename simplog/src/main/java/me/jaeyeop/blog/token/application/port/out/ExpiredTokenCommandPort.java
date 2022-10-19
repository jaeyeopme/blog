package me.jaeyeop.blog.token.application.port.out;

import me.jaeyeop.blog.token.domain.ExpiredToken;

public interface ExpiredTokenCommandPort {

  void expire(ExpiredToken access);

}
