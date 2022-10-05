package me.jaeyeop.blog.auth.application.port.out;

public interface ExpiredTokenQueryPort {

  boolean isExpired(String accessToken);

}
