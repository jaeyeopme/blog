package me.jaeyeop.blog.post.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;
import me.jaeyeop.blog.post.domain.Post;

public interface PostQueryPort {

    boolean existsById(Long id);

    Optional<Post> findById(Long id);

    Optional<PostInformationProjectionDto> findInformationById(Long id);
}
