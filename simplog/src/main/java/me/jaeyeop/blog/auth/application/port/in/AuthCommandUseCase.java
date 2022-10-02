package me.jaeyeop.blog.auth.application.port.in;

public interface AuthCommandUseCase {

  void expireToken(String accessToken, String refreshToken);

}
