package me.jaeyeop.blog.post.application.port.in;

import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;

/**
 * @author jaeyeopme Created on 10/12/2022.
 */
public interface PostQueryUseCase {

    PostInformationProjectionDto findInformationById(InformationQuery informationQuery);

    record InformationQuery(Long postId) {}
}
