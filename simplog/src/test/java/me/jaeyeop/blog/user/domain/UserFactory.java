package me.jaeyeop.blog.user.domain;

public abstract class UserFactory {

  public static final String DEFAULT_USER_EMAIL = "email@email.com";

  private static final String DEFAULT_USER_NAME = "name";

  private static final String DEFAULT_USER_PICTURE = "picture_url";

  private static final Role DEFAULT_USER_ROLE = Role.USER;

  private static final long DEFAULT_USER_ID = 1L;

  public static User createDefault() {
    return User.builder()
        .id(DEFAULT_USER_ID)
        .email(DEFAULT_USER_EMAIL)
        .name(DEFAULT_USER_NAME)
        .picture(DEFAULT_USER_PICTURE)
        .role(DEFAULT_USER_ROLE)
        .build();
  }

  public static User createUpdate(final String name,
      final String picture) {
    return User.builder()
        .id(DEFAULT_USER_ID)
        .email(DEFAULT_USER_EMAIL)
        .name(name)
        .picture(picture)
        .role(DEFAULT_USER_ROLE)
        .build();
  }

}
