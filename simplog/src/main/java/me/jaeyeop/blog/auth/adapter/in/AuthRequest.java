package me.jaeyeop.blog.auth.adapter.in;

public final class AuthRequest {

  private AuthRequest() {
  }

  public record Refresh(String refreshToken) {

  }

  public record Expire(String accessToken, String refreshToken) {

  }

}
