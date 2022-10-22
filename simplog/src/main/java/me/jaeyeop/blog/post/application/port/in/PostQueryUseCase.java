package me.jaeyeop.blog.post.application.port.in;

import static me.jaeyeop.blog.post.adapter.in.PostRequest.Find;
import me.jaeyeop.blog.post.adapter.out.PostResponse.Info;

public interface PostQueryUseCase {

  /**
   * 게시글 정보 단건 조회
   *
   * @param request 게시글 식별자
   * @return 게시글 정보
   */
  Info findOne(Find request);

}
