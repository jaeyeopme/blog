package me.jaeyeop.blog.comment.adapter.out;

import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentJpaRepository extends JpaRepository<Comment, Long> {}
