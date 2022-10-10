package me.jaeyeop.blog.user.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaeyeop.blog.config.jpa.AbstractTimeAuditing;
import me.jaeyeop.blog.config.security.OAuth2Attributes;
import me.jaeyeop.blog.config.security.OAuth2Provider;

@Entity
@Getter
@Builder(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends AbstractTimeAuditing {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false)
  private String name;

  @Column
  private String picture;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Role role;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private OAuth2Provider provider;

  public static User from(final OAuth2Attributes attributes) {
    return User.builder()
        .email(attributes.getEmail())
        .name(attributes.getName())
        .picture(attributes.getPicture())
        .role(Role.USER)
        .provider(attributes.getProvider())
        .build();
  }

  public static User proxy(final Long id) {
    return User.builder()
        .id(id)
        .build();
  }

  public void updateProfile(
      final String name, final String picture) {
    this.name = name;
    this.picture = picture;
  }

}
