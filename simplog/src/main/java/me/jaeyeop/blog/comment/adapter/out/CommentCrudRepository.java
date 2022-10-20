package me.jaeyeop.blog.comment.adapter.out;

import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.repository.CrudRepository;

public interface CommentCrudRepository extends CrudRepository<Comment, Long> {

}
