package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.in.command.CreatePostCommand;
import me.jaeyeop.blog.post.adapter.in.command.UpdatePostCommand;

public interface PostCommandUseCase {

  Long create(Long authorId, CreatePostCommand command);

  void update(Long authorId,
      Long postId,
      UpdatePostCommand command);

}
