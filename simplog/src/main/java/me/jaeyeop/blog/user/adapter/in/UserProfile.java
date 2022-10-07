package me.jaeyeop.blog.user.adapter.in;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.jaeyeop.blog.config.security.OAuth2Provider;
import me.jaeyeop.blog.user.domain.User;

@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfile {

  private String email;

  private String name;

  private String picture;

  private OAuth2Provider provider;

  public static UserProfile from(final User user) {
    return UserProfile.builder()
        .email(user.getEmail())
        .name(user.getName())
        .picture(user.getPicture())
        .provider(user.getProvider())
        .build();
  }

}
