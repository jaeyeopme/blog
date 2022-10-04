package me.jaeyeop.blog.auth.application.port.in;

import me.jaeyeop.blog.auth.domain.Token;

public interface TokenProvideUseCase {

  Token createAccess(String email);

  Token createRefresh(String email);

  Token authenticate(String value);

}
