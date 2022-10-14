package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.command.DeleteUserProfileCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserProfileCommand;

public interface UserCommandUseCase {

  void update(String email, UpdateUserProfileCommand command);

  void delete(DeleteUserProfileCommand command);

}
