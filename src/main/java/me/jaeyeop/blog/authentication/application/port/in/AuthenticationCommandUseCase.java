package me.jaeyeop.blog.authentication.application.port.in;

import me.jaeyeop.blog.commons.authentication.OAuth2Response;

/** Use case interface for authentication-related commands. */
public interface AuthenticationCommandUseCase {
    /**
     * Logs out the user by invalidating the provided access and refresh tokens.
     *
     * @param command The logout command containing the access and refresh tokens.
     */
    void logout(LogoutCommand command);

    /**
     * Refreshes the authentication tokens using a valid refresh token.
     *
     * @param command The refresh command containing the refresh token.
     * @return A new set of access and refresh tokens.
     */
    OAuth2Response refresh(RefreshCommand command);

    record LogoutCommand(String accessToken, String refreshToken) {}

    record RefreshCommand(String refreshToken) {}
}
