package me.jaeyeop.blog.authentication.application.port.out;

import me.jaeyeop.blog.authentication.domain.ExpiredToken;

public interface ExpiredTokenCommandPort {

    void expire(ExpiredToken token);
}
