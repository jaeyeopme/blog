package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.GetProfileCommand;
import me.jaeyeop.blog.user.adapter.in.UserProfile;

public interface UserQueryUseCase {


  UserProfile getProfile(GetProfileCommand command);

}
