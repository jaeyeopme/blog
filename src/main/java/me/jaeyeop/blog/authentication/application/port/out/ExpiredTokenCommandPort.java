package me.jaeyeop.blog.authentication.application.port.out;

import me.jaeyeop.blog.authentication.domain.ExpiredToken;

/** Command port for invalidating Access Tokens. */
public interface ExpiredTokenCommandPort {
    /** Invalidates the token (registers it in the blacklist). */
    void invalidate(ExpiredToken token);
}
