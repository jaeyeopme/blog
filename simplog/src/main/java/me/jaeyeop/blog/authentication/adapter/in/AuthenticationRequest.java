package me.jaeyeop.blog.authentication.adapter.in;

/**
 * @author jaeyeopme Created on 10/22/2022.
 */
public final class AuthenticationRequest {

  private AuthenticationRequest() {
  }

  public record Refresh(String accessToken, String refreshToken) {

  }

  public record Logout(String accessToken, String refreshToken) {

  }

}
