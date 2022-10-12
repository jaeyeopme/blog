package me.jaeyeop.blog.post.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.adapter.out.PostCommandRepository;
import me.jaeyeop.blog.post.adapter.out.PostPersistenceAdapter;
import me.jaeyeop.blog.post.adapter.out.PostQueryRepository;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.domain.PostFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class PostCommandServiceTest {

  private PostCommandRepository postCommandRepository;

  private PostCommandUseCase postCommandUseCase;

  @BeforeEach
  void setUp() {
    postCommandRepository = Mockito.mock(PostCommandRepository.class);
    final var postCommandPort = new PostPersistenceAdapter(postCommandRepository,
        Mockito.mock(PostQueryRepository.class));
    postCommandUseCase = new PostCommandService(postCommandPort);
  }

  @Test
  void 게시글_저장() {
    final var post = PostFactory.createDefault();
    final var command = new CreatePostCommand("title", "content");
    final var expected = post.getId();
    given(postCommandRepository.save(any())).willReturn(post);

    final var actual = postCommandUseCase.create(1L, command);

    assertThat(actual).isEqualTo(expected);
  }

}