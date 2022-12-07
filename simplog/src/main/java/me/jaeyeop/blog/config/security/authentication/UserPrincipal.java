package me.jaeyeop.blog.config.security.authentication;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

/**
 * @author jaeyeopme Created on 09/29/2022.
 */
public record UserPrincipal(User user,
                            Collection<? extends GrantedAuthority> authorities)
    implements OAuth2User {

  public static UserPrincipal from(final User user) {
    return new UserPrincipal(user,
        Collections.singleton(new SimpleGrantedAuthority(user.role().name())));
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Collections.emptyMap();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities();
  }

  @Override
  public String getName() {
    return user.email();
  }

}
