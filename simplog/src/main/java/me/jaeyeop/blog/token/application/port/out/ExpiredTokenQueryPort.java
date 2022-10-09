package me.jaeyeop.blog.token.application.port.out;

public interface ExpiredTokenQueryPort {

  boolean isExpired(String accessToken);

}
