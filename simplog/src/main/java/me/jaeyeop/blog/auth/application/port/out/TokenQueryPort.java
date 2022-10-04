package me.jaeyeop.blog.auth.application.port.out;

public interface TokenQueryPort {

  boolean isExpired(String value);

}
