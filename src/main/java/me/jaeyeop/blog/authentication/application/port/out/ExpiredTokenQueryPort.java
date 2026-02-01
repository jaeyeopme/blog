package me.jaeyeop.blog.authentication.application.port.out;

/** Query port for checking Access Token invalidation status. */
public interface ExpiredTokenQueryPort {
    boolean isInvalidated(String token);
}
