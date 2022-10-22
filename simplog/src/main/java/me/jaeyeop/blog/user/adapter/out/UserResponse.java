package me.jaeyeop.blog.user.adapter.out;

import me.jaeyeop.blog.config.security.authentication.OAuth2Provider;
import me.jaeyeop.blog.user.domain.User;

public final class UserResponse {

  private UserResponse() {
  }

  public record Profile(String email,
                        String name,
                        String picture,
                        OAuth2Provider provider) {

    public static Profile from(final User user) {
      return new Profile(
          user.email(),
          user.name(),
          user.picture(),
          user.provider());
    }

  }

}
