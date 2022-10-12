package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.in.command.GetPostInformationCommand;
import me.jaeyeop.blog.post.adapter.in.response.PostInformation;

public interface PostQueryUseCase {

  PostInformation getInformation(GetPostInformationCommand command);

}
