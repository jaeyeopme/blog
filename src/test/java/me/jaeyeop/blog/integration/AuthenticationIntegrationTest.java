package me.jaeyeop.blog.integration;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import me.jaeyeop.blog.authentication.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.authentication.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.authentication.domain.RefreshToken;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.authentication.domain.TokenClaims;
import me.jaeyeop.blog.commons.token.TokenProvider;
import me.jaeyeop.blog.support.IntegrationTest;
import me.jaeyeop.blog.support.factory.UserSecurityContextFactory.WithPrincipal;
import me.jaeyeop.blog.user.domain.User;

import static me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor.AUTHENTICATION_API_URI;
import static me.jaeyeop.blog.commons.config.web.HttpTokenExtractor.REFRESH_AUTHORIZATION_HEADER;
import static me.jaeyeop.blog.commons.token.TokenProvider.BEARER_TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
class AuthenticationIntegrationTest extends IntegrationTest {
    @Autowired
    protected ExpiredTokenRepository expiredTokenRepository;

    @Autowired
    protected RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @AfterEach
    void tearDown() {
        clearRedis();
    }

    @Test
    @WithPrincipal
    @DisplayName("Logout")
    void logout() throws Exception {
        final var user = getPrincipalUser();
        final var accessTokenValue = getAccessToken(user).value();
        final var refreshTokenValue = getSavedRefreshToken(user).value();

        final var when =
                mockMvc.perform(
                        delete(AUTHENTICATION_API_URI + "/logout")
                                .header(AUTHORIZATION, getHeaderValue(accessTokenValue))
                                .header(
                                        REFRESH_AUTHORIZATION_HEADER,
                                        getHeaderValue(refreshTokenValue)));

        when.andExpectAll(status().isNoContent());
        assertThat(expiredTokenRepository.findById(accessTokenValue)).isPresent();
        assertThat(refreshTokenRepository.findById(refreshTokenValue)).isNotPresent();
    }

    @Test
    @WithPrincipal
    @DisplayName("Reissue access token")
    void reissue_access_token() throws Exception {
        final var user = getPrincipalUser();
        final var refreshTokenValue = getSavedRefreshToken(user).value();

        final var when =
                mockMvc.perform(
                        post(AUTHENTICATION_API_URI + "/refresh")
                                .header(
                                        REFRESH_AUTHORIZATION_HEADER,
                                        getHeaderValue(refreshTokenValue)));

        when.andExpectAll(status().isCreated());
        assertThat(refreshTokenRepository.findById(refreshTokenValue)).isPresent();
    }

    @Test
    @WithPrincipal
    @DisplayName("Reissue access token with invalid refresh token")
    void reissue_access_token_with_invalid_refresh_token() throws Exception {
        final var user = getPrincipalUser();
        final var refreshTokenValue = getRefreshToken(user).value();

        final var when =
                mockMvc.perform(
                        post(AUTHENTICATION_API_URI + "/refresh")
                                .header(
                                        REFRESH_AUTHORIZATION_HEADER,
                                        getHeaderValue(refreshTokenValue)));

        when.andExpectAll(status().isUnauthorized());
        assertThat(refreshTokenRepository.findById(refreshTokenValue)).isNotPresent();
    }

    private String getHeaderValue(final String value) {
        return BEARER_TYPE + value;
    }

    private Token getAccessToken(final User user) {
        final var claims = new TokenClaims(user.id(), user.roles());
        final var accessToken = tokenProvider.createAccess(claims);
        assertThat(expiredTokenRepository.findById(accessToken.value())).isNotPresent();
        return accessToken;
    }

    private Token getRefreshToken(final User user) {
        final var claims = new TokenClaims(user.id(), user.roles());
        final var refreshToken = tokenProvider.createRefresh(claims);
        assertThat(refreshTokenRepository.findById(refreshToken.value())).isNotPresent();
        return refreshToken;
    }

    private Token getSavedRefreshToken(final User user) {
        final var refreshToken = getRefreshToken(user);
        refreshTokenRepository.save(RefreshToken.from(refreshToken));
        return refreshToken;
    }

    private void clearRedis() {
        log.debug("===== Clear Redis =====");
        expiredTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }
}
