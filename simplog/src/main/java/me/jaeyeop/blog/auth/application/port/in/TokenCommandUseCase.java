package me.jaeyeop.blog.auth.application.port.in;

import me.jaeyeop.blog.auth.domain.Token;

public interface TokenCommandUseCase {

  void logout(Token access, Token refresh);

}
