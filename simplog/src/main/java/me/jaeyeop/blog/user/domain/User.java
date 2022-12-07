package me.jaeyeop.blog.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Getter;
import me.jaeyeop.blog.config.jpa.AbstractBaseEntity;
import me.jaeyeop.blog.config.security.authentication.OAuth2Attributes;
import me.jaeyeop.blog.config.security.authentication.OAuth2Provider;
import org.springframework.util.StringUtils;

/**
 * @author jaeyeopme Created on 09/27/2022.
 */
@Entity
@Getter
public class User extends AbstractBaseEntity {

  @NotBlank
  @Column(nullable = false, unique = true)
  private String email;

  @NotBlank
  @Column(nullable = false)
  private String name;

  @Column
  private String picture;

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
      final String email,
      final String name,
      final String picture,
      final Role role,
      final OAuth2Provider provider) {
    this.email = email;
    this.name = name;
    this.picture = picture;
    this.role = role;
    this.provider = provider;
  }

  public static User from(final OAuth2Attributes attributes) {
    return new User(
        attributes.email(),
        attributes.name(),
        attributes.picture(),
        Role.USER,
        attributes.provider());
  }

  public void updateProfile(final String name, final String picture) {
    if (StringUtils.hasText(name)) {
      this.name = name;
    }
    this.picture = picture;
  }

}
