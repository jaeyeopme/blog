package me.jaeyeop.blog.post.application.port.out;

import me.jaeyeop.blog.post.domain.Post;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
public interface PostCommandPort {

  Post create(Post post);

  void delete(Post post);

}
