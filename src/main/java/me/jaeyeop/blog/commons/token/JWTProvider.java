package me.jaeyeop.blog.commons.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Clock;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import me.jaeyeop.blog.authentication.domain.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JWTProvider implements TokenProvider {

    public static final String TYPE = "Bearer ";

    private final Key key;

    private final long accessExp;

    private final long refreshExp;

    private final Clock clock;

    public JWTProvider(
            @Value("${token.key}") final String key,
            @Value("${token.exp.access}") final long accessExp,
            @Value("${token.exp.refresh}") final long refreshExp,
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

    private Token createToken(final String aud, final long tokenExp) {
        final var now = clock.instant();
        final var exp = now.plusMillis(tokenExp);
        final var token =
                Jwts.builder()
                        .setIssuedAt(Date.from(now))
                        .setExpiration(Date.from(exp))
                        .setAudience(aud)
                        .signWith(key)
                        .compact();

        return new Token(token, aud, exp.toEpochMilli());
    }

    @Override
    public Token verify(final String token) {
        try {
            final var value = removeType(token);
            final var claims = getClaims(value);
            return new Token(value, claims.getAudience(), claims.getExpiration().getTime());
        } catch (final JwtException | IllegalArgumentException e) {
            throw new BadCredentialsException("Invalid token");
        }
    }

    private Claims getClaims(final String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .setClock(() -> Date.from(clock.instant()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private String removeType(final String token) {
        if (isInvalidType(token)) {
            throw new IllegalArgumentException();
        }

        return token.substring(TYPE.length());
    }

    private boolean isInvalidType(final String token) {
        return !StringUtils.hasText(token) || !token.startsWith(TYPE);
    }
}
