package me.jaeyeop.blog.post.adapter.out;

import lombok.RequiredArgsConstructor;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostCommandPort {

  private final PostRepository postRepository;

  @Override
  public Post create(final Post post) {
    return postRepository.save(post);
  }

}
