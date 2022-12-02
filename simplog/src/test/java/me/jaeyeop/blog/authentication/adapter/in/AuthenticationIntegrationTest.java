package me.jaeyeop.blog.authentication.adapter.in;

import static me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor.AUTHENTICATION_API_URI;
import static me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor.REFRESH_AUTHORIZATION;
import static me.jaeyeop.blog.config.token.JWTProvider.TYPE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.authentication.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.authentication.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.authentication.domain.RefreshToken;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.support.IntegrationTestSupport;
import me.jaeyeop.blog.support.helper.UserHelper;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jaeyeopme Created on 12/01/2022.
 */
@Slf4j
class AuthenticationIntegrationTest extends IntegrationTestSupport {

  @Autowired
  protected UserRepository userRepository;

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
  void 로그아웃() throws Exception {
    // GIVEN
    final var user = getUser();
    final var accessToken = getAccessToken(user.email());
    final var accessTokenValue = accessToken.value();
    final var refreshToken = getRefreshToken(user.email());
    final var refreshTokenValue = refreshToken.value();

    // WHEN
    final var when = mockMvc.perform(
        delete(AUTHENTICATION_API_URI + "/logout")
            .header(AUTHORIZATION, getHeaderValue(accessTokenValue))
            .header(REFRESH_AUTHORIZATION, getHeaderValue(refreshTokenValue)));

    // THEN
    when.andExpectAll(status().isNoContent());
    assertThat(expiredTokenRepository.findById(accessTokenValue)).isPresent();
    assertThat(refreshTokenRepository.findById(refreshTokenValue)).isNotPresent();
  }

  @Test
  void 엑세스_토큰_재발급() throws Exception {
    // GIVEN
    final var user = getUser();
    final var accessTokenValue = getAccessToken(user.email()).value();
    final var refreshToken = getRefreshToken(user.email());
    refreshTokenRepository.save(RefreshToken.from(refreshToken));
    final var refreshTokenValue = refreshToken.value();

    // WHEN
    final var when = mockMvc.perform(
        post(AUTHENTICATION_API_URI + "/refresh")
            .header(AUTHORIZATION, getHeaderValue(accessTokenValue))
            .header(REFRESH_AUTHORIZATION, getHeaderValue(refreshTokenValue)));

    // THEN
    final var result = when.andExpectAll(status().isCreated());
    final var newAccessToken = result.andReturn().getResponse().getContentAsString();
    assertThatNoException().isThrownBy(() -> tokenProvider.verify(getHeaderValue(newAccessToken)));
    assertThat(expiredTokenRepository.findById(accessTokenValue)).isPresent();
    assertThat(refreshTokenRepository.findById(refreshTokenValue)).isPresent();
  }

  @Test
  void 리프레시_토큰_저장소에_없는_리프레시_토큰으로_엑세스_토큰_재발급() throws Exception {
    // GIVEN
    final var user = getUser();
    final var accessTokenValue = getAccessToken(user.email()).value();
    final var refreshTokenValue = getRefreshToken(user.email()).value();

    // WHEN
    final var when = mockMvc.perform(
        post(AUTHENTICATION_API_URI + "/refresh")
            .header(AUTHORIZATION, getHeaderValue(accessTokenValue))
            .header(REFRESH_AUTHORIZATION, getHeaderValue(refreshTokenValue)));

    // THEN
    when.andExpectAll(status().isUnauthorized());
    assertThat(expiredTokenRepository.findById(accessTokenValue)).isNotPresent();
    assertThat(refreshTokenRepository.findById(refreshTokenValue)).isNotPresent();
  }

  private User getUser() {
    final var user = userRepository.save(UserHelper.create());
    userRepository.save(user);
    return user;
  }

  private String getHeaderValue(final String value) {
    return TYPE + value;
  }

  private Token getAccessToken(final String email) {
    final var accessToken = tokenProvider.createAccess(email);
    assertThat(expiredTokenRepository.findById(accessToken.value())).isNotPresent();
    return accessToken;
  }

  private Token getRefreshToken(final String email) {
    final var refreshToken = tokenProvider.createRefresh(email);
    assertThat(refreshTokenRepository.findById(refreshToken.value())).isNotPresent();
    return refreshToken;
  }
  
  private void clearRedis() {
    log.info("===== Clear Redis =====");
    expiredTokenRepository.deleteAll();
    refreshTokenRepository.deleteAll();
  }

}
