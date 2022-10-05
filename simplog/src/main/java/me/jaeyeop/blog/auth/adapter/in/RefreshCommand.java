package me.jaeyeop.blog.auth.adapter.in;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RefreshCommand {

  private final String refreshToken;

}
