package me.jaeyeop.blog.token.application.port.in;

import me.jaeyeop.blog.token.adapter.in.command.RefreshCommand;

public interface TokenQueryUseCase {

  String refresh(RefreshCommand command);

}
