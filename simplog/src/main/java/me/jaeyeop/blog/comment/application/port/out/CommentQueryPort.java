package me.jaeyeop.blog.comment.application.port.out;

import me.jaeyeop.blog.comment.adapter.out.response.CommentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentQueryPort {


  Page<CommentInfo> findPageInfoByPostId(Long postId, Pageable pageable);

}
