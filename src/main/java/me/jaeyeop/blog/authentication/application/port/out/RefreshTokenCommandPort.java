package me.jaeyeop.blog.authentication.application.port.out;

import me.jaeyeop.blog.authentication.domain.RefreshToken;

public interface RefreshTokenCommandPort {

    void activate(RefreshToken token);

    void expire(RefreshToken token);
}
