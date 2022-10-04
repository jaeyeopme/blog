package me.jaeyeop.blog.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.application.port.in.TokenProvideUseCase;
import me.jaeyeop.blog.auth.application.port.out.TokenCommandPort;
import me.jaeyeop.blog.auth.domain.Token;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {

  private final TokenCommandPort tokenCommandPort;

  private final TokenProvideUseCase tokenProvideUseCase;

  private final ObjectMapper objectMapper;

  @Override
  public void onAuthenticationSuccess(
      final HttpServletRequest request,
      final HttpServletResponse response,
      final Authentication authentication) throws IOException {
    response.setStatus(HttpStatus.OK.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    final var principal = (OAuth2User) authentication.getPrincipal();

    final var access = tokenProvideUseCase.createAccess(principal.getName());
    final var refresh = createRefresh(principal);

    objectMapper.writeValue(response.getWriter(),
        new OAuth2SuccessResponse(access.getValue(), refresh.getValue()));
  }

  private Token createRefresh(final OAuth2User principal) {
    final var refresh = tokenProvideUseCase.createRefresh(principal.getName());
    tokenCommandPort.activate(refresh);
    return refresh;
  }

}
