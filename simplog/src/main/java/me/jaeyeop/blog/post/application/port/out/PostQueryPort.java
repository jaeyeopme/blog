package me.jaeyeop.blog.post.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.post.adapter.out.response.PostInfo;
import me.jaeyeop.blog.post.domain.Post;

public interface PostQueryPort {

  Optional<PostInfo> findInfoById(Long id);

  Optional<Post> findById(Long id);

}
