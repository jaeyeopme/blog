package me.jaeyeop.blog.post.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.post.adapter.in.PostInformationProjectionDto;
import me.jaeyeop.blog.post.domain.Post;

public interface PostQueryPort {

  Optional<PostInformationProjectionDto> findInformationById(Long id);

  Optional<Post> findById(Long id);

  boolean existsById(Long id);

}
