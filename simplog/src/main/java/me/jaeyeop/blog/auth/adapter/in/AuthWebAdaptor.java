package me.jaeyeop.blog.auth.adapter.in;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.application.port.in.TokenCommandUseCase;
import me.jaeyeop.blog.auth.application.port.in.TokenProvideUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AuthWebAdaptor.AUTH_API_URI)
@RequiredArgsConstructor
public class AuthWebAdaptor {

  public static final String AUTH_API_URI = "/api/v1/auth";

  public static final String REFRESH_AUTHORIZATION = "RefreshAuthorization";

  private final TokenCommandUseCase tokenCommandUseCase;

  private final TokenProvideUseCase tokenProvideUseCase;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/logout")
  public void logout(
      @RequestHeader(AUTHORIZATION) final String accessValue,
      @RequestHeader(REFRESH_AUTHORIZATION) final String refreshValue) {
    final var access = tokenProvideUseCase.authenticate(accessValue);
    final var refresh = tokenProvideUseCase.authenticate(refreshValue);

    tokenCommandUseCase.logout(access, refresh);
  }

}
