package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.UserProfile;
import me.jaeyeop.blog.user.adapter.in.command.GetUserProfileCommand;

public interface UserQueryUseCase {

  UserProfile getProfile(GetUserProfileCommand command);

}
