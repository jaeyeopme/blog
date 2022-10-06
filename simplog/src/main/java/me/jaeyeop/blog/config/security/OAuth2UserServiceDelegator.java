package me.jaeyeop.blog.config.security;

import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserServiceDelegator extends DefaultOAuth2UserService {

  private final UserQueryPort userQueryPort;

  private final UserCommandPort userCommandPort;

  @Transactional
  @Override
  public OAuth2User loadUser(final OAuth2UserRequest userRequest)
      throws OAuth2AuthenticationException {
    final var attributes = super.loadUser(userRequest).getAttributes();
    final var provider = OAuth2Provider.get(
        userRequest.getClientRegistration().getRegistrationId());

    final var oAuth2Attributes = OAuth2Attributes.of(provider, attributes);
    final var user = findOrSave(oAuth2Attributes);

    return OAuth2UserPrincipal.from(user);
  }

  private User findOrSave(final OAuth2Attributes oAuth2Attributes) {
    return userQueryPort.findByEmail(oAuth2Attributes.getEmail())
        .orElseGet(() -> userCommandPort.save(User.from(oAuth2Attributes)));
  }

}
