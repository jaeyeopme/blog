package me.jaeyeop.blog.support.factory;

import static me.jaeyeop.blog.support.factory.UserSecurityContextFactory.WithPrincipal;

import jakarta.persistence.EntityManager;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public final class UserSecurityContextFactory implements WithSecurityContextFactory<WithPrincipal> {
    @Autowired private EntityManager entityManager;
    @Autowired private UserRepository userRepository;

    private void clearPersistenceContext() {
        log.debug("===== Clear Persistence Context =====");
        entityManager.flush();
        entityManager.clear();
    }

    @Override
    public SecurityContext createSecurityContext(final WithPrincipal annotation) {
        final User user = userRepository.save(UserFactory.create());
        clearPersistenceContext();

        return createSecurityContext(user);
    }

    private SecurityContext createSecurityContext(final User user) {
        final var context = SecurityContextHolder.createEmptyContext();
        final var userPrincipal = UserPrincipal.from(user);

        context.setAuthentication(
                UsernamePasswordAuthenticationToken.authenticated(
                        userPrincipal, null, userPrincipal.getAuthorities()));
        return context;
    }

    @Target({ElementType.METHOD})
    @Retention(RetentionPolicy.RUNTIME)
    @WithSecurityContext(factory = UserSecurityContextFactory.class)
    public @interface WithPrincipal {}
}
