package me.jaeyeop.blog.auth.application.port.out;

import me.jaeyeop.blog.auth.domain.Token;

public interface TokenCommandPort {

  void expire(Token access, Token refresh);

  void activate(Token refresh);

}
