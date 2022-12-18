package me.jaeyeop.blog.post.adapter.out;

import me.jaeyeop.blog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jaeyeopme Created on 10/10/2022.
 */
public interface PostJpaRepository extends JpaRepository<Post, Long> {

}
