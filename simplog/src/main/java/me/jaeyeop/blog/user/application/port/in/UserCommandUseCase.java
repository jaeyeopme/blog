package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.UserProfile;
import me.jaeyeop.blog.user.adapter.in.command.DeleteUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserProfileCommand;

public interface UserCommandUseCase {

  UserProfile updateProfile(String email, UpdateUserProfileCommand command);

  void deleteProfile(DeleteUserProfileCommand command);

}
