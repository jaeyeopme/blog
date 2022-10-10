package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.in.CreatePostCommand;

public interface PostCommandUseCase {

  Long create(Long authorId, CreatePostCommand command);

}
