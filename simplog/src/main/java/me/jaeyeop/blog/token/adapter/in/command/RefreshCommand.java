package me.jaeyeop.blog.token.adapter.in.command;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class RefreshCommand {

  private final String refreshToken;

  public RefreshCommand(final String refreshToken) {
    this.refreshToken = refreshToken;
  }

}
