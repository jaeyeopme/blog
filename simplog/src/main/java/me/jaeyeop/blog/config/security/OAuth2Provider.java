package me.jaeyeop.blog.config.security;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.common.error.exception.NotSupportedRegistrationIdException;

@Getter
@RequiredArgsConstructor
public enum OAuth2Provider {

  GOOGLE("google", "email", "picture", "name");

  private static final Map<String, OAuth2Provider> PROVIDER_MAP = Arrays.stream(values())
      .collect(
          Collectors.toUnmodifiableMap(OAuth2Provider::getRegistrationId, Function.identity()));

  private final String registrationId;

  private final String emailAttributeKey;

  private final String pictureAttributeKey;

  private final String nameAttributeKey;

  public static OAuth2Provider get(final String registrationId) {
    if (!PROVIDER_MAP.containsKey(registrationId)) {
      throw new NotSupportedRegistrationIdException();
    }

    return PROVIDER_MAP.get(registrationId);
  }

}
