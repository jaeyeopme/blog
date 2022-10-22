package me.jaeyeop.blog.post.application.port.out;

import static me.jaeyeop.blog.post.adapter.out.PostResponse.Info;
import java.util.Optional;
import me.jaeyeop.blog.post.domain.Post;

public interface PostQueryPort {

  Optional<Info> findInfoById(Long id);

  Optional<Post> findById(Long id);

}
