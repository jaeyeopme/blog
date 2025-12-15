package me.jaeyeop.blog.post.adapter.out;

import me.jaeyeop.blog.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<Post, Long> {}
