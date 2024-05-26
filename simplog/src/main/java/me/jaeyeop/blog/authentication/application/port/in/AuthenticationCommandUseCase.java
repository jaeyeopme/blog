package me.jaeyeop.blog.authentication.application.port.in;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
public interface AuthenticationCommandUseCase {

  void logout(LogoutCommand command);

  String refresh(RefreshCommand command);

  record LogoutCommand(String accessToken, String refreshToken) {

  }

  record RefreshCommand(String accessToken, String refreshToken) {

  }

}
