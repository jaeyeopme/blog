package me.jaeyeop.blog.comment.adapter.out;

import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

}
