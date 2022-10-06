package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.GetProfileCommand;

public interface UserQueryUseCase {


  UserProfile getProfile(GetProfileCommand command);

}
