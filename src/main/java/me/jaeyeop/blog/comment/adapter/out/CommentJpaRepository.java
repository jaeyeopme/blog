package me.jaeyeop.blog.comment.adapter.out;

import org.springframework.data.jpa.repository.JpaRepository;

import me.jaeyeop.blog.comment.domain.Comment;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {}
