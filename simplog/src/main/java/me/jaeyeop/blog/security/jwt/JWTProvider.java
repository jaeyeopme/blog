package me.jaeyeop.blog.security.jwt;

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
import org.springframework.security.core.Authentication;
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

  public String issueAccessToken(final Authentication authentication) {
    final var email = (String) authentication.getPrincipal();
    return issue(email, expirationAccess);
  }

  public String issueRefreshToken(final Authentication authentication) {
    final var email = (String) authentication.getPrincipal();
    return issue(email, expirationRefresh);
  }

  private String issue(
      final String email,
      final long expiration) {
    final var now = Instant.now(clock);

    return Jwts.builder()
        .setIssuedAt(Date.from(now))
        .setExpiration(Date.from(now.plusMillis(expiration)))
        .setAudience(email)
        .signWith(key)
        .compact();
  }

  public Optional<String> getEmail(final String token) {
    try {
      return Optional.of(Jwts.parserBuilder()
          .setSigningKey(key)
          .build()
          .parseClaimsJws(token)
          .getBody()
          .getAudience());
    } catch (final JwtException | IllegalArgumentException e) {
      return Optional.empty();
    }
  }

}
