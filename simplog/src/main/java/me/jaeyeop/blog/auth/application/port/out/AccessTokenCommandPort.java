package me.jaeyeop.blog.auth.application.port.out;

import me.jaeyeop.blog.auth.domain.AccessToken;

public interface AccessTokenCommandPort {

  void expire(AccessToken token);

}
