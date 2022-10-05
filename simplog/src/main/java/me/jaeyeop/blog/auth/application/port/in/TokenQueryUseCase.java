package me.jaeyeop.blog.auth.application.port.in;

import me.jaeyeop.blog.auth.adapter.in.RefreshCommand;

public interface TokenQueryUseCase {

  String refresh(RefreshCommand command);

}
