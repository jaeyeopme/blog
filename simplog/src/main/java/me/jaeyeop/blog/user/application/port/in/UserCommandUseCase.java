package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.UpdateProfileCommand;
import me.jaeyeop.blog.user.adapter.in.UserProfile;

public interface UserCommandUseCase {

  UserProfile updateProfile(String email, UpdateProfileCommand command);

}
