package me.jaeyeop.blog.authentication.adapter.in;

import static me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Expire;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import me.jaeyeop.blog.authentication.adapter.in.AuthenticationRequest.Refresh;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationQueryUseCase;
import me.jaeyeop.blog.config.oas.spec.AuthenticationOAS;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthenticationWebAdaptor.AUTHENTICATION_API_URI)
public class AuthenticationWebAdaptor implements AuthenticationOAS {

  public static final String AUTHENTICATION_API_URI = "/v1/auth";

  public static final String REFRESH_AUTHORIZATION = "X-Authorization";

  private final AuthenticationCommandUseCase authenticationCommandUseCase;

  private final AuthenticationQueryUseCase authenticationQueryUseCase;

  public AuthenticationWebAdaptor(
      final AuthenticationCommandUseCase authenticationCommandUseCase,
      final AuthenticationQueryUseCase authenticationQueryUseCase) {
    this.authenticationCommandUseCase = authenticationCommandUseCase;
    this.authenticationQueryUseCase = authenticationQueryUseCase;
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/expire")
  @Override
  public void expire(
      @RequestHeader(AUTHORIZATION) final String accessToken,
      @RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken) {
    final var request = new Expire(accessToken, refreshToken);
    authenticationCommandUseCase.expire(request);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/refresh")
  @Override
  public String refresh(@RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken) {
    final var request = new Refresh(refreshToken);
    return authenticationQueryUseCase.refresh(request);
  }

}
