package me.jaeyeop.blog.authentication.application.port.out;

import me.jaeyeop.blog.authentication.domain.AccessToken;

public interface AccessTokenCommandPort {

  void expire(AccessToken token);

}
