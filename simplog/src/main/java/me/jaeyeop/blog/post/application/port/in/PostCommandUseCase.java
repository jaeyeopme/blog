package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Delete;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Update;

public interface PostCommandUseCase {

  Long create(Long authorId, Create request);

  void update(Long authorId, Long postId, Update request);

  void delete(Long authorId, Delete request);

}
