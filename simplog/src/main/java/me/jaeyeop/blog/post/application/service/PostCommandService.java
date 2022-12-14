package me.jaeyeop.blog.post.application.service;

import javax.transaction.Transactional;
import me.jaeyeop.blog.config.error.exception.PostNotFoundException;
import me.jaeyeop.blog.config.error.exception.UserNotFoundException;
import me.jaeyeop.blog.post.application.port.in.PostCommandUseCase;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Information;
import me.jaeyeop.blog.post.domain.Post;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
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

  private final UserQueryPort userQueryPort;

  public PostCommandService(
      final PostCommandPort postCommandPort,
      final PostQueryPort postQueryPort,
      final UserQueryPort userQueryPort
  ) {
    this.postCommandPort = postCommandPort;
    this.postQueryPort = postQueryPort;
    this.userQueryPort = userQueryPort;
  }

  @Override
  public Long write(final WriteCommand command) {
    final var author = findUserById(command.authorId());
    final var information = new Information(command.title(), command.content());
    final var post = postCommandPort.create(Post.of(author, information));

    return post.id();
  }

  @Override
  public void edit(final EditCommand command) {
    final var post = findById(command.targetId());
    post.confirmAccess(findUserById(command.authorId()));
    post.information().edit(command.newTitle(), command.newContent());
  }

  @Override
  public void delete(final DeleteCommand command) {
    final var post = findById(command.targetId());
    post.confirmAccess(findUserById(command.authorId()));

    postCommandPort.delete(post);
  }

  private Post findById(final Long postId) {
    return postQueryPort.findById(postId)
        .orElseThrow(PostNotFoundException::new);
  }

  private User findUserById(final Long userId) {
    return userQueryPort.findById(userId)
        .orElseThrow(UserNotFoundException::new);
  }

}
