package me.jaeyeop.blog.auth.application.port.out;

public interface AccessTokenQueryPort {

  boolean isExpired(String token);

}
