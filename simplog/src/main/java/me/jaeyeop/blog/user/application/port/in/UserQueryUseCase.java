package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.domain.Profile;

/**
 * @author jaeyeopme Created on 10/06/2022.
 */
public interface UserQueryUseCase {

  Profile findProfileByEmail(ProfileQuery profileQuery);

  record ProfileQuery(String email) {

  }

}
