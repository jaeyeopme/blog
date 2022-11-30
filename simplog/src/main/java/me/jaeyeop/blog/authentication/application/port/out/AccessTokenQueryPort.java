package me.jaeyeop.blog.authentication.application.port.out;

public interface AccessTokenQueryPort {

  boolean isExpired(String token);

}
