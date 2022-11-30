package me.jaeyeop.blog.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import me.jaeyeop.blog.config.jpa.AbstractTimeAuditing;
import me.jaeyeop.blog.config.security.authentication.OAuth2Attributes;
import me.jaeyeop.blog.config.security.authentication.OAuth2Provider;
import org.springframework.util.StringUtils;

@Entity
@Getter
public class User extends AbstractTimeAuditing {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private Long id;

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

  @Builder(access = AccessLevel.PACKAGE)
  private User(final Long id,
      final String email,
      final String name,
      final String picture,
      final Role role,
      final OAuth2Provider provider) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.picture = picture;
    this.role = role;
    this.provider = provider;
  }

  public static User proxy(final Long id) {
    return User.builder()
        .id(id)
        .build();
  }

  public static User from(final OAuth2Attributes attributes) {
    return User.builder()
        .email(attributes.email())
        .name(attributes.name())
        .picture(attributes.picture())
        .role(Role.USER)
        .provider(attributes.provider())
        .build();
  }

  public void updateProfile(final String name, final String picture) {
    if (StringUtils.hasText(name)) {
      this.name = name;
    }
    this.picture = picture;
  }

}
