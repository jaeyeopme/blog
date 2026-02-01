package me.jaeyeop.blog.commons.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.crypto.SecretKey;
import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.authentication.domain.TokenClaims;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JWTProvider implements TokenProvider {
    public static final String ROLES = "roles";

    private final Key key;

    private final long accessExp;

    private final long refreshExp;

    private final Clock clock;

    public JWTProvider(
        @Value("${token.key}")
        final String key,
        @Value("${token.exp.access}")
        final long accessExp,
        @Value("${token.exp.refresh}")
        final long refreshExp,
        final Clock clock) {
        this.key = encode(key);
        this.accessExp = accessExp;
        this.refreshExp = refreshExp;
        this.clock = clock;
    }

    private SecretKey encode(final String secret) {
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(secret.getBytes()));
    }

    private Token createToken(final TokenClaims tokenClaims, final long tokenExp) {
        final var now = clock.instant();
        final var exp = now.plusMillis(tokenExp);
        final var value =
            Jwts.builder()
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .setSubject(String.valueOf(tokenClaims.id()))
                .addClaims(
                    Map.of(
                        ROLES,
                        tokenClaims.authorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList()))
                .signWith(key)
                .compact();

        return new Token(value, tokenClaims, exp.toEpochMilli());
    }

    @Override
    public Token createAccess(final TokenClaims tokenClaims) {
        return createToken(tokenClaims, accessExp);
    }

    @Override
    public Token createRefresh(final TokenClaims tokenClaims) {
        return createToken(tokenClaims, refreshExp);
    }

    @Override
    public Token verify(final String token) {
        try {
            return getToken(token);
        } catch (final ExpiredJwtException e) {
            throw new CredentialsExpiredException("Expired token", e);
        } catch (final JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid token", e);
        }
    }

    private Token getToken(final String value) {
        final var claims = parse(value);
        return createToken(value, claims);
    }

    private Token createToken(final String value, final Claims claims) {
        final var roles = claims.get(ROLES);
        final var list = roles instanceof List<?> ? (List<?>) roles : List.of();
        final var authorities =
            list.stream().map(String::valueOf).map(SimpleGrantedAuthority::new).toList();
        final var expiration = claims.getExpiration().getTime();
        final var tokenClaims = new TokenClaims(Long.valueOf(claims.getSubject()), authorities);

        return new Token(value, tokenClaims, expiration);
    }

    private Claims parse(final String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .setClock(() -> Date.from(clock.instant()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
