package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.in.command.GetPostCommand;
import me.jaeyeop.blog.post.adapter.out.response.PostInfo;

public interface PostQueryUseCase {

  /**
   * 게시글 정보 단건 조회
   *
   * @param command 게시글 식별자
   * @return 게시글 정보
   */
  PostInfo getOne(GetPostCommand command);

}
