package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.post.adapter.in.CreatePostCommand;
import me.jaeyeop.blog.post.adapter.out.PostPersistenceAdapter;
import me.jaeyeop.blog.post.adapter.out.PostRepository;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.domain.PostFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostCommandServiceTest {

  private PostRepository postRepository;

  private PostCommandUseCase postCommandUseCase;

  @BeforeEach
  void setUp() {
    postRepository = Mockito.mock(PostRepository.class);
    final var postCommandPort = new PostPersistenceAdapter(postRepository);
    postCommandUseCase = new PostCommandService(postCommandPort);
  }

  @Test
  void 게시글_저장() {
    final var post = PostFactory.createDefault();
    given(postRepository.save(any())).willReturn(post);
    final var command = new CreatePostCommand("title", "content");
    final var expected = post.getId();

    final var actual = postCommandUseCase.create(1L, command);

    assertThat(actual).isEqualTo(expected);
  }

}