package me.jaeyeop.blog.user.application.port.in;

import me.jaeyeop.blog.user.adapter.in.command.DeleteUserCommand;
import me.jaeyeop.blog.user.adapter.in.command.UpdateUserCommand;

public interface UserCommandUseCase {

  void update(String email, UpdateUserCommand command);

  void delete(DeleteUserCommand command);

}
