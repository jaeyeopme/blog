package me.jaeyeop.blog.commons.authentication;

import jakarta.transaction.Transactional;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class OAuth2UserServiceDelegator extends DefaultOAuth2UserService {

    private final UserQueryPort userQueryPort;

    private final UserCommandPort userCommandPort;

    public OAuth2UserServiceDelegator(
            final UserQueryPort userQueryPort, final UserCommandPort userCommandPort) {
        this.userQueryPort = userQueryPort;
        this.userCommandPort = userCommandPort;
    }

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {
        final var attributes = super.loadUser(userRequest).getAttributes();
        final var provider =
                OAuth2Provider.find(userRequest.getClientRegistration().getRegistrationId());

        final var oAuth2Attributes = OAuth2Attributes.of(provider, attributes);
        final var user = findOrSave(oAuth2Attributes);

        return UserPrincipal.from(user);
    }

    private User findOrSave(final OAuth2Attributes oAuth2Attributes) {
        return userQueryPort
                .findByEmail(oAuth2Attributes.email())
                .orElseGet(() -> userCommandPort.save(User.from(oAuth2Attributes)));
    }
}
