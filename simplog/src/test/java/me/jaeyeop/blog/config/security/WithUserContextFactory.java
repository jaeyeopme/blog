package me.jaeyeop.blog.config.security;

import me.jaeyeop.blog.config.security.authentication.UserPrincipal;
import me.jaeyeop.blog.support.helper.UserHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
public class WithUserContextFactory
    implements WithSecurityContextFactory<WithUser> {

  @Override
  public SecurityContext createSecurityContext(final WithUser annotation) {
    final var context = SecurityContextHolder.createEmptyContext();
    final var principal = UserPrincipal.from(UserHelper.create());

    context.setAuthentication(
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));

    return context;
  }

}
