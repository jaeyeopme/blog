package me.jaeyeop.blog.user.application.port.in;

/**
 * @author jaeyeopme Created on 09/29/2022.
 */
public interface UserCommandUseCase {

  void update(Update command);

  void delete(Delete command);

  record Update(Long targetId, String newName, String newIntroduce) {

  }

  record Delete(Long targetId) {

  }

}
