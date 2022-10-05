package me.jaeyeop.blog.auth.application.port.out;

import me.jaeyeop.blog.auth.adapter.out.ExpiredToken;

public interface ExpiredTokenCommandPort {

  void expire(ExpiredToken access);

}
