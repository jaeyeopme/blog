package me.jaeyeop.blog.user.domain;

public class UserFactory {

  private static final String DEFAULT_EMAIL = "email@email.com";

  private static final String DEFAULT_NAME = "name";

  private static final String DEFAULT_PICTURE = "picture_url";

  private static final Role DEFAULT_ROLE = Role.USER;

  private static final long DEFAULT_ID = 1L;

  public static User create() {
    return User.builder()
        .id(DEFAULT_ID)
        .email(DEFAULT_EMAIL)
        .name(DEFAULT_NAME)
        .picture(DEFAULT_PICTURE)
        .role(DEFAULT_ROLE)
        .build();
  }

}
