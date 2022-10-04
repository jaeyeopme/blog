package me.jaeyeop.blog.auth.application.service;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.auth.application.port.in.TokenCommandUseCase;
import me.jaeyeop.blog.auth.application.port.out.TokenCommandPort;
import me.jaeyeop.blog.auth.domain.Token;
import org.springframework.stereotype.Service;

@Transactional
@Service
@RequiredArgsConstructor
public class TokenCommandService implements TokenCommandUseCase {

  private final TokenCommandPort tokenCommandPort;

  @Override
  public void logout(final Token access, final Token refresh) {
    tokenCommandPort.expire(access, refresh);
  }

}
