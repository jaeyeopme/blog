package me.jaeyeop.blog.support;

import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.ExpiredTokenQueryPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenCommandPort;
import me.jaeyeop.blog.authentication.application.port.out.RefreshTokenQueryPort;
import me.jaeyeop.blog.authentication.application.service.AuthenticationCommandService;
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.comment.application.port.out.CommentQueryPort;
import me.jaeyeop.blog.comment.application.service.CommentCommandService;
import me.jaeyeop.blog.comment.application.service.CommentQueryService;
import me.jaeyeop.blog.config.token.TokenProvider;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.application.service.PostCommandService;
import me.jaeyeop.blog.post.application.service.PostQueryService;
import me.jaeyeop.blog.support.helper.TokenProviderHelper;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.application.service.UserCommandService;
import me.jaeyeop.blog.user.application.service.UserQueryService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;


/**
 * @author jaeyeopme Created on 12/01/2022.
 */
@Disabled
@Tag("unit")
@ExtendWith(MockitoExtension.class)
public abstract class UnitTest {

  @Mock
  protected ExpiredTokenCommandPort expiredTokenCommandPort;

  @Mock(stubOnly = true)
  protected ExpiredTokenQueryPort expiredTokenQueryPort;

  @Mock
  protected RefreshTokenCommandPort refreshTokenCommandPort;

  @Mock(stubOnly = true)
  protected RefreshTokenQueryPort refreshTokenQueryPort;

  @Spy
  protected TokenProvider tokenProvider = TokenProviderHelper.create();

  @InjectMocks
  protected AuthenticationCommandService authCommandService;

  @Mock
  protected UserCommandPort userCommandPort;

  @Mock(stubOnly = true)
  protected UserQueryPort userQueryPort;

  @InjectMocks
  protected UserCommandService userCommandService;

  @InjectMocks
  protected UserQueryService userQueryService;

  @Mock
  protected PostCommandPort postCommandPort;

  @Mock(stubOnly = true)
  protected PostQueryPort postQueryPort;

  @InjectMocks
  protected PostCommandService postCommandService;

  @InjectMocks
  protected PostQueryService postQueryService;

  @Mock
  protected CommentCommandPort commentCommandPort;

  @Mock(stubOnly = true)
  protected CommentQueryPort commentQueryPort;

  @InjectMocks
  protected CommentCommandService commentCommandService;

  @InjectMocks
  protected CommentQueryService commentQueryService;

}
