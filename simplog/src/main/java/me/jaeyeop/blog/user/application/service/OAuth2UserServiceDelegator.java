package me.jaeyeop.blog.user.application.service;

import javax.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.config.security.OAuth2Attributes;
import me.jaeyeop.blog.config.security.OAuth2Provider;
import me.jaeyeop.blog.config.security.OAuth2UserPrincipal;
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
public class OAuth2UserServiceDelegator extends DefaultOAuth2UserService {

  private final UserQueryPort userQueryPort;

  private final UserCommandPort userCommandPort;

  public OAuth2UserServiceDelegator(final UserQueryPort userQueryPort,
      final UserCommandPort userCommandPort) {
    this.userQueryPort = userQueryPort;
    this.userCommandPort = userCommandPort;
  }

  @Transactional
  @Override
  public OAuth2User loadUser(final OAuth2UserRequest userRequest)
      throws OAuth2AuthenticationException {
    final var attributes = super.loadUser(userRequest).getAttributes();
    final var provider = OAuth2Provider.get(
        userRequest.getClientRegistration().getRegistrationId());

    final var oAuth2Attributes = OAuth2Attributes.of(provider, attributes);
    final var user = findOrSave(oAuth2Attributes);

    return OAuth2UserPrincipal.of(user);
  }

  private User findOrSave(final OAuth2Attributes oAuth2Attributes) {
    if (userQueryPort.existsByEmail(oAuth2Attributes.getEmail())) {
      return userQueryPort.findByEmail(oAuth2Attributes.getEmail());
    }

    return userCommandPort.save(User.of(oAuth2Attributes));
  }

}
