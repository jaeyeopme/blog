package me.jaeyeop.blog.user.application.port.in;

import static me.jaeyeop.blog.user.adapter.in.UserRequest.Find;
import me.jaeyeop.blog.user.adapter.out.UserResponse.Profile;

public interface UserQueryUseCase {

  /**
   * 사용자 프로필 조회 <br/><br/> 인가 검증에서 사용자를 조회했기 때문에 추가적인 DTO Projection 없이 조회
   *
   * @param request 사용자 프로필 조회 명령
   * @return 사용자 프로필
   */
  Profile findOneByEmail(Find request);

}
