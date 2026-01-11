package me.jaeyeop.blog.support.factory;

import me.jaeyeop.blog.commons.authentication.OAuth2Attributes;
import me.jaeyeop.blog.commons.authentication.OAuth2Provider;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.test.util.ReflectionTestUtils;

public final class UserFactory {
    private static final String DEFAULT_EMAIL = "email@email.com";
    private static final String DEFAULT_NAME = "name";
    private static final String DEFAULT_PICTURE = "picture";

    public static User create() {
        return create(DEFAULT_EMAIL);
    }

    public static User create(final String email) {
        final var user =
                User.from(
                        new OAuth2Attributes(
                                OAuth2Provider.GOOGLE, email, DEFAULT_NAME, DEFAULT_PICTURE));
        ReflectionTestUtils.setField(user.profile(), "name", "name");
        ReflectionTestUtils.setField(user.profile(), "introduce", "introduce");
        return user;
    }
}
