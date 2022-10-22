package me.jaeyeop.blog.auth.application.port.out;

import me.jaeyeop.blog.auth.domain.RefreshToken;

public interface RefreshTokenCommandPort {

  void activate(RefreshToken token);

  void expire(RefreshToken token);

}
