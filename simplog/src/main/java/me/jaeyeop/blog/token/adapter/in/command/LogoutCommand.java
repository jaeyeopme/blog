package me.jaeyeop.blog.token.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class LogoutCommand {

  private final String accessToken;

  private final String refreshToken;

  public LogoutCommand(final String accessToken,
      final String refreshToken) {
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
  }

}
