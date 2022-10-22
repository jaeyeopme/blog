package me.jaeyeop.blog.auth.application.port.in;

import me.jaeyeop.blog.auth.adapter.in.AuthRequest.Expire;

public interface AuthCommandUseCase {

  void expire(Expire request);

}
