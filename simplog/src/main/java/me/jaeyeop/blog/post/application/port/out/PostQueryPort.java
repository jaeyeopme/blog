package me.jaeyeop.blog.post.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;
import me.jaeyeop.blog.post.domain.Post;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
public interface PostQueryPort {

  boolean existsById(Long id);

  Optional<Post> findById(Long id);

  Optional<PostInformationProjectionDto> findInformationById(Long id);

}
