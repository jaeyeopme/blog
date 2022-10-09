package me.jaeyeop.blog.token.application.port.out;

import me.jaeyeop.blog.token.adapter.out.RefreshToken;

public interface RefreshTokenCommandPort {

  void expire(RefreshToken refresh);

  void activateRefresh(RefreshToken refresh);

}
