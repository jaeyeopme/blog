package me.jaeyeop.blog.commons.authentication;

import java.util.Map;

public record OAuth2Attributes(OAuth2Provider provider, String email, String name, String picture) {
    public static OAuth2Attributes of(
            final OAuth2Provider provider, final Map<String, Object> attributes) {
        return new OAuth2Attributes(
                provider,
                (String) attributes.get(provider.emailAttributeKey()),
                (String) attributes.get(provider.nameAttributeKey()),
                (String) attributes.get(provider.pictureAttributeKey()));
    }
}
