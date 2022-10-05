package me.jaeyeop.blog.auth.application.port.out;

public interface RefreshTokenQueryPort {

  boolean isExpired(String refreshToken);

}
