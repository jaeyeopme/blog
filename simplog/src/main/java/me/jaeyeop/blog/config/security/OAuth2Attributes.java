package me.jaeyeop.blog.config.security;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@Builder(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuth2Attributes {

  private final OAuth2Provider provider;

  private final String email;

  private final String name;

  private final String picture;

  public static OAuth2Attributes of(final OAuth2Provider provider,
      final Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
        .provider(provider)
        .email((String) attributes.get(provider.getEmailAttributeKey()))
        .name((String) attributes.get(provider.getNameAttributeKey()))
        .picture((String) attributes.get(provider.getPictureAttributeKey()))
        .build();
  }

}
