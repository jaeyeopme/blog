package me.jaeyeop.blog.token.application.port.in;

import me.jaeyeop.blog.token.adapter.in.LogoutCommand;

public interface TokenCommandUseCase {

  void logout(LogoutCommand command);

}
