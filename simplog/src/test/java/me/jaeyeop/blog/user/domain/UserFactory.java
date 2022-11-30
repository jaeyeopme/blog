package me.jaeyeop.blog.user.domain;

import me.jaeyeop.blog.config.security.authentication.OAuth2Provider;

public abstract class UserFactory {

  private static final String DEFAULT_USER_EMAIL = "email@email.com";

  private static final String DEFAULT_USER_NAME = "name";

  private static final String DEFAULT_USER_PICTURE = "picture";

  private static final Role DEFAULT_USER_ROLE = Role.USER;

  public static User createUser1() {
    return User.builder()
        .id(1L)
        .email(DEFAULT_USER_EMAIL)
        .name(DEFAULT_USER_NAME)
        .picture(DEFAULT_USER_PICTURE)
        .role(DEFAULT_USER_ROLE)
        .provider(OAuth2Provider.GOOGLE)
        .build();
  }

  public static User createUser1(final String email) {
    return User.builder()
        .id(1L)
        .email(email)
        .name(DEFAULT_USER_NAME)
        .picture(DEFAULT_USER_PICTURE)
        .role(DEFAULT_USER_ROLE)
        .provider(OAuth2Provider.GOOGLE)
        .build();
  }

  public static User createUser2() {
    return User.builder()
        .id(2L)
        .email(DEFAULT_USER_EMAIL)
        .name(DEFAULT_USER_NAME)
        .picture(DEFAULT_USER_PICTURE)
        .role(DEFAULT_USER_ROLE)
        .provider(OAuth2Provider.GOOGLE)
        .build();
  }

}
