package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.command.GetUserCommand;
import me.jaeyeop.blog.user.adapter.out.response.UserProfile;

public interface UserQueryUseCase {

  /**
   * 사용자 프로필 조회 <br/><br/> 인가 검증에서 사용자를 조회했기 때문에 추가적인 DTO Projection 없이 조회
   *
   * @param command 사용자 프로필 조회 명령
   * @return 사용자 프로필
   */
  UserProfile getOneByEmail(GetUserCommand command);

}
