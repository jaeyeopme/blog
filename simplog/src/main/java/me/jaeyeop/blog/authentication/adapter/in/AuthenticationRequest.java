package me.jaeyeop.blog.authentication.adapter.in;

public final class AuthenticationRequest {

  private AuthenticationRequest() {
  }

  public record Refresh(String refreshToken) {

  }

  public record Expire(String accessToken, String refreshToken) {

  }

}
