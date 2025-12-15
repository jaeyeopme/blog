package me.jaeyeop.blog.support;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import me.jaeyeop.blog.comment.adapter.out.CommentJpaRepository;
import me.jaeyeop.blog.comment.domain.Comment;
import me.jaeyeop.blog.commons.config.security.UserPrincipal;
import me.jaeyeop.blog.post.adapter.out.PostJpaRepository;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.support.helper.CommentHelper;
import me.jaeyeop.blog.support.helper.PostHelper;
import me.jaeyeop.blog.user.adapter.out.UserRepository;
import me.jaeyeop.blog.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@Slf4j
@Disabled
@Tag("integration")
@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class IntegrationTest extends ContainerTest {

    @Autowired protected WebApplicationContext context;

    @Autowired protected MockMvc mockMvc;

    @Autowired protected ObjectMapper objectMapper;

    @Autowired protected EntityManager entityManager;

    @Autowired protected UserRepository userRepository;

    @Autowired protected PostJpaRepository postJpaRepository;

    @Autowired protected CommentJpaRepository commentJpaRepository;

    @BeforeEach
    public void setUp() {
        this.mockMvc =
                MockMvcBuilders.webAppContextSetup(this.context)
                        .alwaysDo(print())
                        .alwaysDo(result -> clearPersistenceContext())
                        .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                        .build();
    }

    protected void clearPersistenceContext() {
        entityManager.flush();
        entityManager.clear();
    }

    protected String toJson(final Object value) throws JsonProcessingException {
        return objectMapper.writeValueAsString(value);
    }

    protected User getPrincipal() {
        return ((UserPrincipal)
                        SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .user();
    }

    protected Post getPost(final User author) {
        final var post = postJpaRepository.save(PostHelper.create(author));
        clearPersistenceContext();
        return post;
    }

    protected Comment getComment(final Post post, final User author) {
        final var comment = commentJpaRepository.save(CommentHelper.create(post, author));
        clearPersistenceContext();
        return comment;
    }
}
