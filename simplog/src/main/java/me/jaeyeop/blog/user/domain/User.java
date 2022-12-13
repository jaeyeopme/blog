package me.jaeyeop.blog.user.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import me.jaeyeop.blog.config.jpa.AbstractBaseEntity;
import me.jaeyeop.blog.config.security.authentication.OAuth2Attributes;
import me.jaeyeop.blog.config.security.authentication.OAuth2Provider;

/**
 * @author jaeyeopme Created on 09/27/2022.
 */
@Entity
@Getter
public class User extends AbstractBaseEntity {

  @Embedded
  private Profile profile;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @NotNull
  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OAuth2Provider provider;

  protected User() {
  }

  private User(
      final Profile profile,
      final Role role,
      final OAuth2Provider provider
  ) {
    this.profile = profile;
    this.role = role;
    this.provider = provider;
  }

  public static User from(final OAuth2Attributes attributes) {
    return new User(
        new Profile(attributes.email(), attributes.name(), attributes.picture()),
        Role.USER,
        attributes.provider());
  }

}
