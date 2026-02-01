package me.jaeyeop.blog.authentication.domain;

import java.time.Instant;

/**
 * Represents an authentication token with its value, claims, and expiration time.
 *
 * @param value The string value of the token.
 * @param tokenClaims The claims associated with the token.
 * @param expiration The expiration time of the token in milliseconds since the epoch.
 */
public record Token(String value, TokenClaims tokenClaims, long expiration) {
    /**
     * Checks if the token has expired.
     *
     * @return true if the current time is past the expiration time, false otherwise.
     */
    public boolean isExpired() {
        return expiration < Instant.now().toEpochMilli();
    }

    /**
     * Calculates the remaining time until the token expires.
     *
     * @return The remaining time in milliseconds. If the token is expired, this may be negative.
     */
    public long remainingMillis() {
        return expiration - Instant.now().toEpochMilli();
    }
}
