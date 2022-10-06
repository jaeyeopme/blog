package me.jaeyeop.blog.user.application.port.in;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.config.security.OAuth2Provider;
import me.jaeyeop.blog.user.domain.User;

@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfile {

  private final String email;

  private final String name;

  private final String picture;

  private final OAuth2Provider provider;

  public static UserProfile from(final User user) {
    return UserProfile.builder()
        .email(user.getEmail())
        .name(user.getName())
        .picture(user.getPicture())
        .provider(user.getProvider())
        .build();
  }

}
