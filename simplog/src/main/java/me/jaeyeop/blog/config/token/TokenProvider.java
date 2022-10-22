package me.jaeyeop.blog.config.token;

import me.jaeyeop.blog.auth.domain.Token;

public interface TokenProvider {

  Token createAccess(String email);

  Token createRefresh(String email);

  Token authenticate(String token);

}
