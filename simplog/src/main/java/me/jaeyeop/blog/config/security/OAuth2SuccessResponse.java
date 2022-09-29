package me.jaeyeop.blog.config.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuth2SuccessResponse {

  private final String accessToken;

  private final String refreshToken;

}
