package me.jaeyeop.blog.config.security;

import me.jaeyeop.blog.user.domain.UserFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithOAuth2UserContextFactory
    implements WithSecurityContextFactory<WithUser1> {

  @Override
  public SecurityContext createSecurityContext(final WithUser1 annotation) {
    final var context = SecurityContextHolder.createEmptyContext();
    final var principal = OAuth2UserPrincipal.from(UserFactory.createUser1());

    context.setAuthentication(
        new UsernamePasswordAuthenticationToken(principal, null, principal.getAuthorities()));

    return context;
  }

}
