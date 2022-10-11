package me.jaeyeop.blog.post.adapter.out;

import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class PostPersistenceAdapter implements PostCommandPort {

  private final PostRepository postRepository;

  public PostPersistenceAdapter(final PostRepository postRepository) {
    this.postRepository = postRepository;
  }

  @Override
  public Post create(final Post post) {
    return postRepository.save(post);
  }

}
