package me.jaeyeop.blog.post.adapter.out;

import me.jaeyeop.blog.post.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostRepository extends CrudRepository<Post, Long> {

}
