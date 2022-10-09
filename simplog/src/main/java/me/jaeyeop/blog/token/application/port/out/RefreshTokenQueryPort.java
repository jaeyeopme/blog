package me.jaeyeop.blog.token.application.port.out;

public interface RefreshTokenQueryPort {

  boolean isExpired(String refreshToken);

}
