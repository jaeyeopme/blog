package me.jaeyeop.blog.comment.application.port.in;

import me.jaeyeop.blog.comment.adapter.out.response.CommentInfo;
import me.jaeyeop.blog.post.adapter.in.command.GetCommentsCommand;
import org.springframework.data.domain.Page;

public interface CommentQueryUseCase {

  /**
   * 댓글 정보 페이지 조회
   *
   * @param command 게시글 식별자, 페이지 옵션
   * @return 댓글 정보 페이지
   */
  Page<CommentInfo> getPage(GetCommentsCommand command);

}
