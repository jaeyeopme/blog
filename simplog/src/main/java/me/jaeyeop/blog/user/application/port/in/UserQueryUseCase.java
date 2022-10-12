package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.command.GetUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.response.UserProfile;

public interface UserQueryUseCase {

  UserProfile getProfile(GetUserProfileCommand command);

}
