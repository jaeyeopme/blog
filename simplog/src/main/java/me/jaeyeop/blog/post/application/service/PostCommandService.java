package me.jaeyeop.blog.post.application.service;

import static me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Delete;
import static me.jaeyeop.blog.post.adapter.in.PostRequest.Update;
import javax.transaction.Transactional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;
import org.springframework.stereotype.Service;

@Transactional
@Service
public class PostCommandService implements PostCommandUseCase {

  private final PostCommandPort postCommandPort;

  private final PostQueryPort postQueryPort;

  public PostCommandService(final PostCommandPort postCommandPort,
      final PostQueryPort postQueryPort) {
    this.postCommandPort = postCommandPort;
    this.postQueryPort = postQueryPort;
  }

  @Override
  public Long create(final Long authorId, final Create request) {
    final var post = Post.of(authorId, request.title(), request.content());
    return postCommandPort.create(post).id();
  }

  @Override
  public void update(
      final Long authorId,
      final Long postId,
      final Update request) {
    final var post = findById(authorId, postId);

    post.updateInformation(request.title(), request.content());
  }

  @Override
  public void delete(final Long authorId, final Delete request) {
    final var post = findById(authorId, request.postId());

    postCommandPort.delete(post);
  }

  private Post findById(final Long authorId, final Long postId) {
    final var post = postQueryPort.findById(postId)
        .orElseThrow(PostNotFoundException::new);

    post.confirmAccess(authorId);

    return post;
  }

}
