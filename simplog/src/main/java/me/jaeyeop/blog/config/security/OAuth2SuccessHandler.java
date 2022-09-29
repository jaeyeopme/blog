package me.jaeyeop.blog.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final ObjectMapper objectMapper;

  private final JWTProvider jwtProvider;

  @SuppressWarnings("deprecation")
  @Override
  public void onAuthenticationSuccess(final HttpServletRequest request,
      final HttpServletResponse response,
      final Authentication authentication) throws IOException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    final var principal = (OAuth2User) authentication.getPrincipal();

    final var accessToken = jwtProvider.issueAccessToken(principal.getName());
    final var refreshToken = jwtProvider.issueRefreshToken(principal.getName());

    objectMapper.writeValue(response.getWriter(),
        new OAuth2SuccessResponse(accessToken, refreshToken));
  }

}
