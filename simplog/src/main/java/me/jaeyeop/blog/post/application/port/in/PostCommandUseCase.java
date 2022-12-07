package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.in.PostRequest.Create;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Delete;
import me.jaeyeop.blog.post.adapter.in.PostRequest.Update;
import me.jaeyeop.blog.user.domain.User;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
public interface PostCommandUseCase {

  Long create(User author, Create request);

  void update(User author, Long postId, Update request);

  void delete(User author, Delete request);

}
