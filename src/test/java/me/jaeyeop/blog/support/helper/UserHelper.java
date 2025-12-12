package me.jaeyeop.blog.support.helper;

import static me.jaeyeop.blog.support.helper.UserHelper.WithPrincipal;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.commons.authentication.OAuth2Attributes;
import me.jaeyeop.blog.commons.authentication.OAuth2Provider;
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
import org.springframework.test.util.ReflectionTestUtils;

/**
 * @author jaeyeopme Created on 12/01/2022.
 */
@Slf4j
@Component
public final class UserHelper implements WithSecurityContextFactory<WithPrincipal> {

  private static final String DEFAULT_EMAIL = "email@email.com";
  private static final String DEFAULT_NAME = "name";
  private static final String DEFAULT_PICTURE = "picture";

  @Autowired
  private EntityManager entityManager;

  @Autowired
  private UserRepository userRepository;

  public static User create() {
    return create(DEFAULT_EMAIL);
  }

  public static User create(final String email) {
    final var user = User.from(
        new OAuth2Attributes(
            OAuth2Provider.GOOGLE,
            email,
            DEFAULT_NAME,
            DEFAULT_PICTURE
        )
    );
    ReflectionTestUtils.setField(user.profile(), "name", "name");
    ReflectionTestUtils.setField(user.profile(), "introduce", "introduce");
    return user;
  }

  private void clearPersistenceContext() {
    log.info("===== Clear Persistence Context =====");
    entityManager.flush();
    entityManager.clear();
  }

  @Override
  public SecurityContext createSecurityContext(final WithPrincipal annotation) {
    final var user = userRepository.save(UserHelper.create());
    clearPersistenceContext();

    return createSecurityContext(user);
  }

  private SecurityContext createSecurityContext(final User user) {
    final var context = SecurityContextHolder.createEmptyContext();
    final var userPrincipal = UserPrincipal.from(user);

    context.setAuthentication(new UsernamePasswordAuthenticationToken(
        userPrincipal, null, userPrincipal.getAuthorities()));
    return context;
  }

  @Target({ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  @WithSecurityContext(factory = UserHelper.class)
  public @interface WithPrincipal {

  }

}
