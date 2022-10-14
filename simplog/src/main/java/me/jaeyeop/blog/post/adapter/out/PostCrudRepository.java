package me.jaeyeop.blog.post.adapter.out;

import me.jaeyeop.blog.post.domain.Post;
import org.springframework.data.repository.CrudRepository;

public interface PostCrudRepository extends CrudRepository<Post, Long> {

}
