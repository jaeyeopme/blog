package me.jaeyeop.blog.authentication.application.port.in;

import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Expire;

public interface AuthenticationCommandUseCase {

  void expire(Expire request);

}
