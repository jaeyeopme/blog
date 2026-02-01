package me.jaeyeop.blog.authentication.application.port.out;

import me.jaeyeop.blog.authentication.domain.RefreshToken;

public interface RefreshTokenCommandPort {
    void invalidate(RefreshToken token);

    void activate(RefreshToken token);
}
