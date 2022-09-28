package me.jaeyeop.blog.security.authentication;

import java.util.Collections;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.jaeyeop.blog.user.domain.Role;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@EqualsAndHashCode(callSuper = false)
@Getter
public class UserAuthenticationToken extends AbstractAuthenticationToken {

  private String value;

  private transient Object principal;

  private transient Object credentials;

  private UserAuthenticationToken(final String value) {
    super(null);
    this.value = value;
    this.setAuthenticated(false);
  }

  private UserAuthenticationToken(final String email,
      final Role role) {
    super(Collections.singletonList(new SimpleGrantedAuthority(role.name())));
    this.principal = email;
    this.credentials = Strings.EMPTY;
    this.setAuthenticated(true);
  }

  public static UserAuthenticationToken unauthenticated(final String value) {
    return new UserAuthenticationToken(value);
  }

  public static UserAuthenticationToken authenticated(final String email,
      final Role role) {
    return new UserAuthenticationToken(email, role);
  }

}
