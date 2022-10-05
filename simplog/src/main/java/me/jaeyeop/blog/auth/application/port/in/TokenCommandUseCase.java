package me.jaeyeop.blog.auth.application.port.in;

import me.jaeyeop.blog.auth.adapter.in.LogoutCommand;

public interface TokenCommandUseCase {

  void logout(LogoutCommand command);

}
