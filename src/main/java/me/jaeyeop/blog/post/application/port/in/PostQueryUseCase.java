package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;

public interface PostQueryUseCase {
    PostInformationProjectionDto findInformationById(InformationQuery informationQuery);

    record InformationQuery(Long postId) {}
}
