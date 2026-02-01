package me.jaeyeop.blog.authentication.adapter.out;

import org.springframework.stereotype.Component;

import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenQueryPort;
import me.jaeyeop.blog.authentication.domain.ExpiredToken;

@Component
public class AccessTokenPersistenceAdapter
        implements ExpiredTokenCommandPort, ExpiredTokenQueryPort {
    private final ExpiredTokenRepository expiredTokenRepository;

    public AccessTokenPersistenceAdapter(final ExpiredTokenRepository expiredTokenRepository) {
        this.expiredTokenRepository = expiredTokenRepository;
    }

    @Override
    public void invalidate(final ExpiredToken token) {
        expiredTokenRepository.save(token);
    }

    @Override
    public boolean isInvalidated(final String token) {
        return expiredTokenRepository.existsById(token);
    }
}
