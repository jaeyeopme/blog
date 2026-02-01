package me.jaeyeop.blog.commons.token;

import me.jaeyeop.blog.authentication.domain.Token;
import me.jaeyeop.blog.authentication.domain.TokenClaims;

/** Interface for token generation and verification. */
public interface TokenProvider {
    String BEARER_TYPE = "Bearer ";

    /**
     * Creates an access token with the given claims.
     *
     * @param tokenClaims The claims to include in the token.
     * @return The generated access token.
     */
    Token createAccess(TokenClaims tokenClaims);

    /**
     * Creates a refresh token with the given claims.
     *
     * @param tokenClaims The claims to include in the token.
     * @return The generated refresh token.
     */
    Token createRefresh(TokenClaims tokenClaims);

    /**
     * Verifies the given token string and returns a Token object.
     *
     * @param token The token string to verify.
     * @return The verified Token object.
     * @throws me.jaeyeop.blog.commons.token.ExpiredTokenException if the token is expired.
     * @throws org.springframework.security.authentication.BadCredentialsException if the token is
     *     invalid.
     */
    Token verify(String token);
}
