package me.jaeyeop.blog.post.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.post.adapter.in.response.PostInformation;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class PostPersistenceAdapter implements PostCommandPort, PostQueryPort {

  private final PostCommandRepository postCommandRepository;

  private final PostQueryRepository postQueryRepository;

  public PostPersistenceAdapter(final PostCommandRepository postCommandRepository,
      final PostQueryRepository postQueryRepository) {
    this.postCommandRepository = postCommandRepository;
    this.postQueryRepository = postQueryRepository;
  }

  @Override
  public Post create(final Post post) {
    return postCommandRepository.save(post);
  }

  @Override
  public Optional<PostInformation> getInformationById(final Long id) {
    return postQueryRepository.getInformationById(id);
  }

}
