package me.jaeyeop.blog.authentication.application.port.out;

public interface ExpiredTokenQueryPort {

    boolean isExpired(String token);
}
