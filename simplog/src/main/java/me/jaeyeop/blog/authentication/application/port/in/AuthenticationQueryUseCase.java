package me.jaeyeop.blog.authentication.application.port.in;

import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Refresh;

public interface AuthenticationQueryUseCase {

  String refresh(Refresh request);

}
