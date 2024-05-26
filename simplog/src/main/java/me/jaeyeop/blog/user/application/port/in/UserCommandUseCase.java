package me.jaeyeop.blog.user.application.port.in;

/**
 * @author jaeyeopme Created on 09/29/2022.
 */
public interface UserCommandUseCase {

  void update(UpdateCommand command);

  void delete(DeleteCommand command);

  record UpdateCommand(Long targetId, String newName, String newIntroduce) {

  }

  record DeleteCommand(Long targetId) {

  }

}
