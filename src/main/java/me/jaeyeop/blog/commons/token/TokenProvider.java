package me.jaeyeop.blog.commons.token;

import me.jaeyeop.blog.authentication.domain.Token;

public interface TokenProvider {

    Token createAccess(String email);

    Token createRefresh(String email);

    Token verify(String token);
}
