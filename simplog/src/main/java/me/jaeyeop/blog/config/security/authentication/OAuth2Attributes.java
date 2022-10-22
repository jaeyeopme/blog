package me.jaeyeop.blog.config.security.authentication;

import java.util.Map;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class OAuth2Attributes {

  private final OAuth2Provider provider;

  private final String email;

  private final String name;

  private final String picture;

  @Builder(access = AccessLevel.PRIVATE)
  private OAuth2Attributes(final OAuth2Provider provider,
      final String email,
      final String name,
      final String picture) {
    this.provider = provider;
    this.email = email;
    this.name = name;
    this.picture = picture;
  }


  public static OAuth2Attributes of(final OAuth2Provider provider,
      final Map<String, Object> attributes) {
    return OAuth2Attributes.builder()
        .provider(provider)
        .email((String) attributes.get(provider.emailAttributeKey()))
        .name((String) attributes.get(provider.nameAttributeKey()))
        .picture((String) attributes.get(provider.pictureAttributeKey()))
        .build();
  }

}
