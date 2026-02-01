package me.jaeyeop.blog.authentication.adapter.in;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.LogoutCommand;
import me.jaeyeop.blog.authentication.application.port.in.AuthenticationCommandUseCase.RefreshCommand;
import me.jaeyeop.blog.commons.authentication.OAuth2Response;
import me.jaeyeop.blog.commons.config.web.ExtractToken;
import me.jaeyeop.blog.commons.token.TokenType;

import static me.jaeyeop.blog.authentication.adapter.in.AuthenticationWebAdaptor.AUTHENTICATION_API_URI;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

@RestController
@RequestMapping(AUTHENTICATION_API_URI)
public class AuthenticationWebAdaptor implements AuthenticationOAS {
    public static final String AUTHENTICATION_API_URI = "/api/v1/auth";

    private final AuthenticationCommandUseCase authenticationCommandUseCase;

    public AuthenticationWebAdaptor(
            final AuthenticationCommandUseCase authenticationCommandUseCase) {
        this.authenticationCommandUseCase = authenticationCommandUseCase;
    }

    @ResponseStatus(NO_CONTENT)
    @DeleteMapping("/logout")
    @Override
    public void logout(
            @ExtractToken(TokenType.ACCESS)
            final String accessToken,
            @ExtractToken(TokenType.REFRESH)
            final String refreshToken) {
        final var command = new LogoutCommand(accessToken, refreshToken);
        authenticationCommandUseCase.logout(command);
    }

    @ResponseStatus(CREATED)
    @PostMapping("/refresh")
    @Override
    public OAuth2Response refresh(@ExtractToken(TokenType.REFRESH) final String refreshToken) {
        final var command = new RefreshCommand(refreshToken);
        return authenticationCommandUseCase.refresh(command);
    }
}
