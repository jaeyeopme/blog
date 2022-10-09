package me.jaeyeop.blog.token.adapter.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshCommand {

  private final String refreshToken;

}
