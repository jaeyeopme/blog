package me.jaeyeop.blog.comment.application.port.in;

import me.jaeyeop.blog.comment.adapter.in.CommentRequest.Find;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import org.springframework.data.domain.Page;

public interface CommentQueryUseCase {

  /**
   * 댓글 정보 페이지 조회
   *
   * @param request 게시글 식별자, 페이지 옵션
   * @return 댓글 정보 페이지
   */
  Page<Info> findCommentPage(Find request);

}
