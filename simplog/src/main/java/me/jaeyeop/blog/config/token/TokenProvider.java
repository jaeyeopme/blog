package me.jaeyeop.blog.config.token;

import me.jaeyeop.blog.authentication.domain.Token;

/**
 * @author jaeyeopme Created on 09/28/2022.
 */
public interface TokenProvider {

  Token createAccess(String email);

  Token createRefresh(String email);

  Token verify(String token);

}
