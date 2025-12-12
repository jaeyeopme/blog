package me.jaeyeop.blog.user.application.service;

import jakarta.transaction.Transactional;
import me.jaeyeop.blog.comment.application.port.out.CommentCommandPort;
import me.jaeyeop.blog.commons.error.exception.UserNotFoundException;
import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.user.application.port.in.UserCommandUseCase;
import me.jaeyeop.blog.user.application.port.out.UserCommandPort;
import me.jaeyeop.blog.user.application.port.out.UserQueryPort;
import me.jaeyeop.blog.user.domain.User;
import org.springframework.stereotype.Service;

/**
 * @author jaeyeopme Created on 09/29/2022.
 */
@Transactional
@Service
public class UserCommandService implements UserCommandUseCase {

  private final UserQueryPort userQueryPort;

  private final UserCommandPort userCommandPort;

  private final PostCommandPort postCommandPort;

  private final CommentCommandPort commentCommandPort;

  public UserCommandService(
      final UserQueryPort userQueryPort,
      final UserCommandPort userCommandPort,
      final PostCommandPort postCommandPort,
      final CommentCommandPort commentCommandPort
  ) {
    this.userQueryPort = userQueryPort;
    this.userCommandPort = userCommandPort;
    this.postCommandPort = postCommandPort;
    this.commentCommandPort = commentCommandPort;
  }

  @Override
  public void update(final UpdateCommand command) {
    final var profile = findById(command.targetId()).profile();
    profile.update(command.newName(), command.newIntroduce());
  }

  @Override
  public void delete(final DeleteCommand command) {
    final var user = findById(command.targetId());
    preRemove(user);
    userCommandPort.delete(user);
  }

  private User findById(final Long id) {
    return userQueryPort.findById(id)
        .orElseThrow(UserNotFoundException::new);
  }

  private void preRemove(final User user) {
    commentCommandPort.deleteAll(user.comments());
    postCommandPort.deleteAll(user.posts());
  }

}
