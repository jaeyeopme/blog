package me.jaeyeop.blog.config;

import me.jaeyeop.blog.comment.adapter.out.CommentCrudRepository;
import me.jaeyeop.blog.comment.adapter.out.CommentPersistenceAdapter;
import me.jaeyeop.blog.comment.adapter.out.CommentQueryRepository;
import me.jaeyeop.blog.post.adapter.out.PostCrudRepository;
import me.jaeyeop.blog.post.adapter.out.PostPersistenceAdapter;
import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
import me.jaeyeop.blog.token.adapter.out.ExpiredTokenPersistenceAdapter;
import me.jaeyeop.blog.token.adapter.out.ExpiredTokenRepository;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenPersistenceAdapter;
import me.jaeyeop.blog.token.adapter.out.RefreshTokenRepository;
import me.jaeyeop.blog.user.adapter.out.UserPersistenceAdapter;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.context.annotation.Import;

@Import({
    ExpiredTokenPersistenceAdapter.class,
    RefreshTokenPersistenceAdapter.class,
    UserPersistenceAdapter.class,
    PostPersistenceAdapter.class,
    CommentPersistenceAdapter.class})
@MockBeans({
    @MockBean(ExpiredTokenRepository.class),
    @MockBean(RefreshTokenRepository.class),
    @MockBean(UserRepository.class),
    @MockBean(PostCrudRepository.class),
    @MockBean(PostQueryRepository.class),
    @MockBean(CommentCrudRepository.class),
    @MockBean(CommentQueryRepository.class)})
@TestConfiguration
public class MockRepositoryTestConfig {

}
