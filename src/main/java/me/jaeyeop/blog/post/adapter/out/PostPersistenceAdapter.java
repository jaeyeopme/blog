package me.jaeyeop.blog.post.adapter.out;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import me.jaeyeop.blog.post.application.port.out.PostCommandPort;
import me.jaeyeop.blog.post.application.port.out.PostQueryPort;
import me.jaeyeop.blog.post.domain.Post;

@Component
public class PostPersistenceAdapter implements PostCommandPort, PostQueryPort {
    private final PostJpaRepository postJpaRepository;

    private final PostQueryRepository postQueryRepository;

    public PostPersistenceAdapter(
            final PostJpaRepository postJpaRepository,
            final PostQueryRepository postQueryRepository) {
        this.postJpaRepository = postJpaRepository;
        this.postQueryRepository = postQueryRepository;
    }

    @Override
    public Post create(final Post post) {
        return postJpaRepository.save(post);
    }

    @Override
    public void delete(final Post post) {
        postJpaRepository.delete(post);
    }

    @Override
    public void deleteAll(final List<Post> posts) {
        postJpaRepository.deleteAllInBatch(posts);
    }

    @Override
    public boolean existsById(final Long id) {
        return postJpaRepository.existsById(id);
    }

    @Override
    public Optional<Post> findById(final Long id) {
        return postJpaRepository.findById(id);
    }

    @Override
    public Optional<PostInformationProjectionDto> findInformationById(final Long id) {
        return postQueryRepository.findInfoById(id);
    }
}
