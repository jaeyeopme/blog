package me.jaeyeop.blog.authentication.application.port.out;

public interface RefreshTokenQueryPort {

  boolean isExpired(String token);

}
