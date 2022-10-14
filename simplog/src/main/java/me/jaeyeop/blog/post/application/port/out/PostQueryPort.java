package me.jaeyeop.blog.post.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.post.adapter.in.response.PostInformation;
import me.jaeyeop.blog.post.domain.Post;

public interface PostQueryPort {

  Optional<PostInformation> getPostInformationById(Long id);

  Optional<Post> findById(Long id);

}
