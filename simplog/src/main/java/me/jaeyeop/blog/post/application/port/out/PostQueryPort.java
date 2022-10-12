package me.jaeyeop.blog.post.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.post.adapter.in.response.PostInformation;

public interface PostQueryPort {

  Optional<PostInformation> getInformationById(Long id);

}
