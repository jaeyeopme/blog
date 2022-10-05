package me.jaeyeop.blog.auth.application.port.out;

import me.jaeyeop.blog.auth.adapter.out.RefreshToken;

public interface RefreshTokenCommandPort {

  void expire(RefreshToken refresh);

  void activateRefresh(RefreshToken refresh);

}
