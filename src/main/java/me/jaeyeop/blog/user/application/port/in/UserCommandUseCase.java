package me.jaeyeop.blog.user.application.port.in;

public interface UserCommandUseCase {

    void update(UpdateCommand command);

    void delete(DeleteCommand command);

    record UpdateCommand(Long targetId, String newName, String newIntroduce) {}

    record DeleteCommand(Long targetId) {}
}
