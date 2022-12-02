package me.jaeyeop.blog.support.helper;

import me.jaeyeop.blog.config.security.authentication.OAuth2Attributes;
import me.jaeyeop.blog.config.security.authentication.OAuth2Provider;
import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 12/01/2022.
 */
public final class UserHelper {

  private static final String DEFAULT_EMAIL = "email@email.com";

  private static final String DEFAULT_NAME = "name";

  private static final String DEFAULT_PICTURE = "picture";

  private UserHelper() {
  }

  public static User create() {
    return User.from(getAttributes());
  }

  private static OAuth2Attributes getAttributes() {
    return new OAuth2Attributes(
        OAuth2Provider.GOOGLE,
        DEFAULT_EMAIL,
        DEFAULT_NAME,
        DEFAULT_PICTURE);
  }

}
