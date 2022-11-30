package me.jaeyeop.blog.user.application.port.in;

import static me.jaeyeop.blog.user.adapter.in.UserRequest.Delete;
import static me.jaeyeop.blog.user.adapter.in.UserRequest.Update;

public interface UserCommandUseCase {

  void delete(Delete request);

  void update(String email, Update request);

}
