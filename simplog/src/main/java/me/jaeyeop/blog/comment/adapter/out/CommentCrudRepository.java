package me.jaeyeop.blog.comment.adapter.out;

import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.repository.CrudRepository;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
public interface CommentCrudRepository extends CrudRepository<Comment, Long> {

}
