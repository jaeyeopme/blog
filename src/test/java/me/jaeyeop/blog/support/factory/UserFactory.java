package me.jaeyeop.blog.support.factory;

import org.springframework.test.util.ReflectionTestUtils;

import me.jaeyeop.blog.commons.authentication.OAuth2Attributes;
import me.jaeyeop.blog.commons.authentication.OAuth2Provider;
import me.jaeyeop.blog.user.domain.User;

public final class UserFactory {
    private static final String DEFAULT_EMAIL = "email@email.com";

    private static final String DEFAULT_NAME = "name";

    private static final String DEFAULT_PICTURE = "picture";

    public static User create() {
        final var user =
                User.from(
                        new OAuth2Attributes(
                                OAuth2Provider.GOOGLE,
                                DEFAULT_EMAIL,
                                DEFAULT_NAME,
                                DEFAULT_PICTURE));
        ReflectionTestUtils.setField(user.profile(), "name", "name");
        ReflectionTestUtils.setField(user.profile(), "introduce", "introduce");
        return user;
    }

    public static User create(Long id) {
        final var user =
                User.from(
                        new OAuth2Attributes(
                                OAuth2Provider.GOOGLE,
                                DEFAULT_EMAIL,
                                DEFAULT_NAME,
                                DEFAULT_PICTURE));
        ReflectionTestUtils.setField(user, "id", id);
        ReflectionTestUtils.setField(user.profile(), "name", "name");
        ReflectionTestUtils.setField(user.profile(), "introduce", "introduce");
        return user;
    }
}
