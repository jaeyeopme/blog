package me.jaeyeop.blog.auth.application.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import me.jaeyeop.blog.auth.application.port.in.TokenProvideUseCase;
import me.jaeyeop.blog.auth.domain.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class TokenProvideService implements TokenProvideUseCase {

  private static final String TYPE = "Bearer ";

  private final Key key;

  private final long accessExp;

  private final long refreshExp;

  private final Clock clock;

  public TokenProvideService(
      @Value("${jwt.key}") final String key,
      @Value("${jwt.exp.access}") final long accessExp,
      @Value("${jwt.exp.refresh}") final long refreshExp,
      final Clock clock) {
    this.key = encode(key);
    this.accessExp = accessExp;
    this.refreshExp = refreshExp;
    this.clock = clock;
  }

  private SecretKey encode(final String secret) {
    return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
  }

  @Override
  public Token createAccess(final String email) {
    return createToken(email, accessExp);
  }

  @Override
  public Token createRefresh(final String email) {
    return createToken(email, refreshExp);
  }

  private Token createToken(
      final String email,
      final long tokenExp) {
    final var iat = clock.instant();
    final var exp = iat.plusMillis(tokenExp);
    final var value = createJwts(email, iat, exp);

    return new Token(TYPE + value, email, exp.toEpochMilli());
  }

  private String createJwts(
      final String aud,
      final Instant iat,
      final Instant exp) {
    return Jwts.builder()
        .setIssuedAt(Date.from(iat))
        .setExpiration(Date.from(exp))
        .setAudience(aud)
        .signWith(key)
        .compact();
  }

  private Claims getClaims(final String value)
      throws JwtException, IllegalArgumentException {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .setClock(() -> Date.from(clock.instant()))
        .build()
        .parseClaimsJws(value)
        .getBody();
  }

  @Override
  public Token authenticate(final String value) {
    try {
      final var claims = getClaims(removeType(value));
      return new Token(value, claims.getAudience(), claims.getExpiration().getTime());
    } catch (final JwtException | IllegalArgumentException e) {
      throw new BadCredentialsException("Bad credentials");
    }
  }

  private String removeType(final String value) {
    if (isInvalidType(value)) {
      throw new IllegalArgumentException();
    }

    return value.substring(TYPE.length());
  }

  private boolean isInvalidType(final String value) {
    return !StringUtils.hasText(value) || !value.startsWith(TYPE);
  }

}
