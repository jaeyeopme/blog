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
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Service;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
@Transactional
@Service
public class PostCommandService implements PostCommandUseCase {

  private final PostCommandPort postCommandPort;

  private final PostQueryPort postQueryPort;

  public PostCommandService(
      final PostCommandPort postCommandPort,
      final PostQueryPort postQueryPort) {
    this.postCommandPort = postCommandPort;
    this.postQueryPort = postQueryPort;
  }

  @Override
  public Long create(final User author, final Create request) {
    final var post = Post.of(request.title(), request.content(), author);
    return postCommandPort.create(post).id();
  }

  @Override
  public void update(
      final User author,
      final Long postId,
      final Update request) {
    final var post = findById(author.id(), postId);

    post.updateInformation(request.title(), request.content());
  }

  @Override
  public void delete(final User author, final Delete request) {
    final var post = findById(author.id(), request.postId());

    postCommandPort.delete(post);
  }

  private Post findById(final Long authorId, final Long postId) {
    final var post = postQueryPort.findById(postId)
        .orElseThrow(PostNotFoundException::new);

    post.confirmAccess(authorId);

    return post;
  }

}
