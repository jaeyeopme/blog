package me.jaeyeop.blog.authentication.application.port.out;

/** Query port for checking Refresh Token invalidation status. */
public interface RefreshTokenQueryPort {
    /** Checks if the token is invalidated. */
    boolean isInvalidated(String token);
}
