package me.jaeyeop.blog.authentication.adapter.in;

import static me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor.AUTHENTICATION_API_URI;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.LogoutCommand;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.RefreshCommand;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author jaeyeopme Created on 10/02/2022.
 */
@RestController
@RequestMapping(AUTHENTICATION_API_URI)
public class AuthenticationWebAdaptor implements AuthenticationOAS {

  public static final String AUTHENTICATION_API_URI = "/api/v1/auth";
  public static final String REFRESH_AUTHORIZATION = "Refresh-Authorization";

  private final AuthenticationCommandUseCase authenticationCommandUseCase;

  public AuthenticationWebAdaptor(final AuthenticationCommandUseCase authenticationCommandUseCase) {
    this.authenticationCommandUseCase = authenticationCommandUseCase;
  }

  @ResponseStatus(NO_CONTENT)
  @DeleteMapping("/logout")
  @Override
  public void logout(
      @RequestHeader(AUTHORIZATION) final String accessToken,
      @RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken
  ) {
    final var command = new LogoutCommand(accessToken, refreshToken);
    authenticationCommandUseCase.logout(command);
  }

  @ResponseStatus(CREATED)
  @PostMapping("/refresh")
  @Override
  public String refresh(
      @RequestHeader(AUTHORIZATION) final String accessToken,
      @RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken
  ) {
    final var command = new RefreshCommand(accessToken, refreshToken);
    return authenticationCommandUseCase.refresh(command);
  }

}
