package me.jaeyeop.blog.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.token.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.token.domain.RefreshToken;
import me.jaeyeop.blog.token.domain.Token;
import me.jaeyeop.blog.user.adapter.in.UserWebAdapter;
import org.springframework.http.HttpHeaders;
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

  public OAuth2SuccessHandler(final RefreshTokenCommandPort refreshTokenCommandPort,
      final TokenProvider tokenProvider, final ObjectMapper objectMapper) {
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
    final OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

    final Token accessToken = tokenProvider.createAccess(principal.getName());
    final Token refreshToken = createRefresh(principal);

    final String createdURI = String.format("%s/%s", UserWebAdapter.USER_API_URI,
        principal.getEmail());
    response.addHeader(HttpHeaders.LOCATION, createdURI);

    objectMapper.writeValue(response.getWriter(),
        new OAuth2Success(accessToken.getValue(), refreshToken.getValue()));
  }

  private Token createRefresh(final OAuth2UserPrincipal principal) {
    final Token token = tokenProvider.createRefresh(principal.getName());
    refreshTokenCommandPort.activateRefresh(
        new RefreshToken(token.getValue(), token.getExpiration()));
    return token;
  }

}
