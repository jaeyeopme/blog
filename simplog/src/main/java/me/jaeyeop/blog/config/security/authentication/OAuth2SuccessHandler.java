package me.jaeyeop.blog.config.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.domain.RefreshToken;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final RefreshTokenCommandPort refreshTokenCommandPort;

  private final TokenProvider tokenProvider;

  private final ObjectMapper objectMapper;

  public OAuth2SuccessHandler(
      final RefreshTokenCommandPort refreshTokenCommandPort,
      final TokenProvider tokenProvider,
      final ObjectMapper objectMapper) {
    this.refreshTokenCommandPort = refreshTokenCommandPort;
    this.tokenProvider = tokenProvider;
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationSuccess(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Authentication authentication) throws IOException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    final var principal = (UserPrincipal) authentication.getPrincipal();

    final var accessToken = tokenProvider.createAccess(principal.email());
    final var refreshToken = createRefresh(principal);

    objectMapper.writeValue(response.getWriter(),
        new OAuth2Response(accessToken.value(), refreshToken.value()));
  }

  private Token createRefresh(final UserPrincipal principal) {
    final var token = tokenProvider.createRefresh(principal.email());
    refreshTokenCommandPort.activate(new RefreshToken(token.value(), token.expiration()));
    return token;
  }

}
