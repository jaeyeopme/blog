package me.jaeyeop.blog.comment.application.port.out;

import java.util.Optional;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import me.jaeyeop.blog.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryPort {

  Page<Info> findInfoPageByPostId(Long postId, Pageable pageable);

  Optional<Comment> findById(Long id);

}
