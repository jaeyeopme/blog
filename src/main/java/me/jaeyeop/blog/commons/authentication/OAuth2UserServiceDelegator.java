package me.jaeyeop.blog.commons.authentication;

import jakarta.transaction.Transactional;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.commons.error.exception.OAuth2ProviderMismatchException;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;

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
        final var oAuth2User = super.loadUser(userRequest);
        final var attributes = oAuth2User.getAttributes();
        final var provider =
                OAuth2Provider.find(userRequest.getClientRegistration().getRegistrationId());
        final var oAuth2Attributes = OAuth2Attributes.of(provider, attributes);
        final var user = findOrSave(oAuth2Attributes);

        return new UserPrincipal(user.id(), user.roles());
    }

    private User findOrSave(final OAuth2Attributes oAuth2Attributes) {
        return userQueryPort
                .findByEmail(oAuth2Attributes.email())
                .map(user -> validateProvider(user, oAuth2Attributes))
                .orElseGet(() -> userCommandPort.save(User.from(oAuth2Attributes)));
    }

    private User validateProvider(final User user, final OAuth2Attributes attributes) {
        if (user.provider() != attributes.provider()) {
            throw new OAuth2ProviderMismatchException(user.provider().registrationId());
        }
        return user;
    }
}
