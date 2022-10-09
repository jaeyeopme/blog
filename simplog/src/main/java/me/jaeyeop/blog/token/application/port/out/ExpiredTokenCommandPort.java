package me.jaeyeop.blog.token.application.port.out;

import me.jaeyeop.blog.token.adapter.out.ExpiredToken;

public interface ExpiredTokenCommandPort {

  void expire(ExpiredToken access);

}
