package me.jaeyeop.blog.auth.application.port.in;

import me.jaeyeop.blog.auth.adapter.in.AuthRequest.Refresh;

public interface AuthQueryUseCase {

  String refresh(Refresh request);

}
