package me.jaeyeop.blog.post.adapter.out;

import java.util.Optional;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Component;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
@Component
public class PostPersistenceAdapter implements PostCommandPort, PostQueryPort {

  private final PostCrudRepository postCrudRepository;

  private final PostQueryRepository postQueryRepository;

  public PostPersistenceAdapter(
      final PostCrudRepository postCrudRepository,
      final PostQueryRepository postQueryRepository
  ) {
    this.postCrudRepository = postCrudRepository;
    this.postQueryRepository = postQueryRepository;
  }

  @Override
  public Post create(final Post post) {
    return postCrudRepository.save(post);
  }

  @Override
  public void delete(final Post post) {
    postCrudRepository.delete(post);
  }

  @Override
  public boolean existsById(final Long id) {
    return postCrudRepository.existsById(id);
  }

  @Override
  public Optional<Post> findById(final Long id) {
    return postCrudRepository.findById(id);
  }

  @Override
  public Optional<PostInformationProjectionDto> findInformationById(final Long id) {
    return postQueryRepository.findInfoById(id);
  }

}
