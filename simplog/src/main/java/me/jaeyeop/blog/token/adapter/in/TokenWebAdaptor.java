package me.jaeyeop.blog.token.adapter.in;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.token.application.port.in.TokenCommandUseCase;
import me.jaeyeop.blog.token.application.port.in.TokenQueryUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(TokenWebAdaptor.AUTH_API_URI)
@RequiredArgsConstructor
public class TokenWebAdaptor {

  public static final String AUTH_API_URI = "/api/v1/auth";

  public static final String REFRESH_AUTHORIZATION = "RefreshAuthorization";

  private final TokenCommandUseCase tokenCommandUseCase;

  private final TokenQueryUseCase tokenQueryUseCase;

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/logout")
  public void logout(
      @RequestHeader(AUTHORIZATION) final String access,
      @RequestHeader(REFRESH_AUTHORIZATION) final String refresh) {
    final var command = new LogoutCommand(access, refresh);
    tokenCommandUseCase.logout(command);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @GetMapping("/refresh")
  public String refresh(
      @RequestHeader(REFRESH_AUTHORIZATION) final String refresh) {
    final var command = new RefreshCommand(refresh);
    return tokenQueryUseCase.refresh(command);
  }

}
