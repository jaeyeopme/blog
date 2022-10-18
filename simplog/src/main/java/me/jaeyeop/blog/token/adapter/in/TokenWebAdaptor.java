package me.jaeyeop.blog.token.adapter.in;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import me.jaeyeop.blog.token.adapter.in.command.LogoutCommand;
import me.jaeyeop.blog.token.adapter.in.command.RefreshCommand;
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
public class TokenWebAdaptor {

  public static final String AUTH_API_URI = "/api/v1/auth";

  public static final String REFRESH_AUTHORIZATION = "Refresh-Authorization";

  private final TokenCommandUseCase tokenCommandUseCase;

  private final TokenQueryUseCase tokenQueryUseCase;

  public TokenWebAdaptor(final TokenCommandUseCase tokenCommandUseCase,
      final TokenQueryUseCase tokenQueryUseCase) {
    this.tokenCommandUseCase = tokenCommandUseCase;
    this.tokenQueryUseCase = tokenQueryUseCase;
  }

  @ResponseStatus(HttpStatus.OK)
  @GetMapping("/logout")
  public void logout(@RequestHeader(AUTHORIZATION) final String accessToken,
      @RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken) {
    final LogoutCommand command = new LogoutCommand(accessToken, refreshToken);
    tokenCommandUseCase.logout(command);
  }

  @ResponseStatus(HttpStatus.CREATED)
  @GetMapping("/refresh")
  public String refresh(@RequestHeader(REFRESH_AUTHORIZATION) final String refreshToken) {
    final RefreshCommand command = new RefreshCommand(refreshToken);
    return tokenQueryUseCase.refresh(command);
  }

}
