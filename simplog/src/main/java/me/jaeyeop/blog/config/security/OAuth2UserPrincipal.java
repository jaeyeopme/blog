package me.jaeyeop.blog.config.security;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2UserPrincipal implements OAuth2User {

  private final Long id;

  private final String email;

  private final Collection<? extends GrantedAuthority> authorities;

  public static OAuth2UserPrincipal from(final User user) {
    return OAuth2UserPrincipal.builder()
        .id(user.getId())
        .email(user.getEmail())
        .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
        .build();
  }

  @Override
  public Map<String, Object> getAttributes() {
    return Collections.emptyMap();
  }

  @Override
  public String getName() {
    return this.email;
  }

}
