package me.jaeyeop.blog.post.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeop.blog.post.domain.Post;

public interface PostJpaRepository extends JpaRepository<Post, Long> {}
