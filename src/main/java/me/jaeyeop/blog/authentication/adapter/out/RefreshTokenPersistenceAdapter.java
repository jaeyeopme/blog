package me.jaeyeop.blog.authentication.adapter.out;

import org.springframework.stereotype.Component;

import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.RefreshToken;

@Component
public class RefreshTokenPersistenceAdapter
        implements RefreshTokenCommandPort, RefreshTokenQueryPort {
    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenPersistenceAdapter(final RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public void activate(final RefreshToken token) {
        refreshTokenRepository.save(token);
    }

    @Override
    public void invalidate(final RefreshToken token) {
        refreshTokenRepository.delete(token);
    }

    @Override
    public boolean isInvalidated(final String token) {
        return !refreshTokenRepository.existsById(token);
    }
}
