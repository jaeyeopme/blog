package me.jaeyeop.blog.auth.adapter.in;

import static me.jaeyeop.blog.auth.adapter.in.AuthRequest.Expire;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import me.jaeyeop.blog.auth.adapter.in.AuthRequest.Refresh;
import me.jaeyeop.blog.auth.application.port.in.AuthCommandUseCase;
import me.jaeyeop.blog.auth.application.port.in.AuthQueryUseCase;
import me.jaeyeop.blog.config.openapi.spec.AuthOpenApiSpec;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthWebAdaptor.AUTH_API_URI)
public class AuthWebAdaptor implements AuthOpenApiSpec {

  public static final String AUTH_API_URI = "/v1/auth";

  public static final String REFRESH_AUTHORIZATION = "Refresh-Authorization";

  private final AuthCommandUseCase authCommandUseCase;

  private final AuthQueryUseCase authQueryUseCase;

  public AuthWebAdaptor(final AuthCommandUseCase authCommandUseCase,
      final AuthQueryUseCase authQueryUseCase) {
    this.authCommandUseCase = authCommandUseCase;
    this.authQueryUseCase = authQueryUseCase;
  }

  @ResponseStatus(HttpStatus.NO_CONTENT)
  @DeleteMapping("/expire")
  @Override
  public void expire(
      @RequestHeader(AUTHORIZATION) final String accessToken,
      @RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken) {
    final var request = new Expire(accessToken, refreshToken);
    authCommandUseCase.expire(request);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping("/refresh")
  @Override
  public String refresh(@RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken) {
    final var request = new Refresh(refreshToken);
    return authQueryUseCase.refresh(request);
  }

}
