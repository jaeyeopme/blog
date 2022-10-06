package me.jaeyeop.blog.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.adapter.out.RefreshToken;
import me.jaeyeop.blog.auth.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.auth.domain.Token;
import me.jaeyeop.blog.config.token.TokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final RefreshTokenCommandPort refreshTokenCommandPort;

  private final TokenProvider tokenProvider;

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Authentication authentication) throws IOException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    final var principal = (OAuth2UserPrincipal) authentication.getPrincipal();

    final var accessToken = tokenProvider.createAccess(principal.getName());
    final var refreshToken = createRefresh(principal);

    objectMapper.writeValue(response.getWriter(),
        new OAuth2Success(accessToken.getValue(), refreshToken.getValue()));
  }

  private Token createRefresh(final OAuth2UserPrincipal principal) {
    final var token = tokenProvider.createRefresh(principal.getName());
    refreshTokenCommandPort.activateRefresh(
        new RefreshToken(token.getValue(), token.getExpiration()));
    return token;
  }

}
