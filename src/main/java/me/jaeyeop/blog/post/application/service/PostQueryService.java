package me.jaeyeop.blog.post.application.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import me.jaeyeop.blog.commons.error.exception.PostNotFoundException;
import me.jaeyeop.blog.post.adapter.out.PostInformationProjectionDto;
import me.jaeyeop.blog.post.application.port.in.PostQueryUseCase;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;

@Transactional(readOnly = true)
@Service
public class PostQueryService implements PostQueryUseCase {
    private final PostQueryPort postQueryPort;

    public PostQueryService(final PostQueryPort postQueryPort) {
        this.postQueryPort = postQueryPort;
    }

    @Override
    public PostInformationProjectionDto findInformationById(
            final InformationQuery informationQuery) {
        return postQueryPort
                .findInformationById(informationQuery.postId())
                .orElseThrow(PostNotFoundException::new);
    }
}
