package me.jaeyeop.blog.token.adapter.in;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class LogoutCommand {

  private final String accessToken;

  private final String refreshToken;

}
