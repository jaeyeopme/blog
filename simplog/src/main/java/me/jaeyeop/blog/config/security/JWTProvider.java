package me.jaeyeop.blog.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JWTProvider {

  private final Clock clock;

  private final Key key;

  private final long expirationAccess;

  private final long expirationRefresh;

  public JWTProvider(
      final Clock clock,
      @Value("${jwt.key}") final String key,
      @Value("${jwt.expiration.access}") final long expirationAccess,
      @Value("${jwt.expiration.refresh}") final long expirationRefresh) {
    this.clock = clock;
    this.key = getKey(key);
    this.expirationAccess = expirationAccess;
    this.expirationRefresh = expirationRefresh;
  }

  private SecretKey getKey(final String secret) {
    return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
  }

  public String issueAccessToken(final String email) {
    return issue(email, expirationAccess);
  }

  public String issueRefreshToken(final String email) {
    return issue(email, expirationRefresh);
  }

  private String issue(
      final String email,
      final long expiration) {
    final var now = clock.instant();

    return Jwts.builder()
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusMillis(expiration)))
        .setAudience(email)
        .signWith(key)
        .compact();
  }

  public Optional<String> getEmail(final String token) {
    try {
      return Optional.of(getClaimsJws(token, clock.instant())
          .getBody()
          .getAudience());
    } catch (final JwtException | IllegalArgumentException e) {
      return Optional.empty();
    }
  }

  private Jws<Claims> getClaimsJws(
      final String token,
      final Instant instant) {
    return Jwts.parserBuilder()
        .setSigningKey(key)
        .setClock(() -> Date.from(instant))
        .build()
        .parseClaimsJws(token);
  }

  public long getRemaining(
      final String token,
      final Instant instant) {
    try {
      final var expiration = getClaimsJws(token, instant).getBody().getExpiration();
      return expiration.getTime() - instant.toEpochMilli();
    } catch (final ExpiredJwtException e) {
      return 0L;
    }
  }

}
