package me.jaeyeop.blog.config.security;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import me.jaeyeop.blog.config.error.exception.NotSupportedRegistrationIdException;

@Getter
public enum OAuth2Provider {

  GOOGLE("google", "email", "picture", "name");

  private static final Map<String, OAuth2Provider> PROVIDER_MAP = Arrays.stream(values())
      .collect(
          Collectors.toUnmodifiableMap(OAuth2Provider::getRegistrationId, Function.identity()));

  private final String registrationId;

  private final String emailAttributeKey;

  private final String pictureAttributeKey;

  private final String nameAttributeKey;

  OAuth2Provider(final String registrationId,
      final String emailAttributeKey,
      final String pictureAttributeKey,
      final String nameAttributeKey) {
    this.registrationId = registrationId;
    this.emailAttributeKey = emailAttributeKey;
    this.pictureAttributeKey = pictureAttributeKey;
    this.nameAttributeKey = nameAttributeKey;
  }

  public static OAuth2Provider get(final String registrationId) {
    if (!PROVIDER_MAP.containsKey(registrationId)) {
      throw new NotSupportedRegistrationIdException();
    }

    return PROVIDER_MAP.get(registrationId);
  }

}
