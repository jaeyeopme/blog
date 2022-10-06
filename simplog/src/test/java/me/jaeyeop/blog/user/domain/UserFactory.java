package me.jaeyeop.blog.user.domain;

public class UserFactory {

  public static final String DEFAULT_USER_EMAIL = "email@email.com";

  public static final String DEFAULT_USER_NAME = "name";

  public static final String DEFAULT_USER_PICTURE = "picture_url";

  public static final Role DEFAULT_USER_ROLE = Role.USER;

  public static final long DEFAULT_USER_ID = 1L;

  public static User createDefault() {
    return User.builder()
        .id(DEFAULT_USER_ID)
        .email(DEFAULT_USER_EMAIL)
        .name(DEFAULT_USER_NAME)
        .picture(DEFAULT_USER_PICTURE)
        .role(DEFAULT_USER_ROLE)
        .build();
  }

}
