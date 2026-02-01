package me.jaeyeop.blog.post.application.port.out;

import java.util.List;

import me.jaeyeop.blog.post.domain.Post;

public interface PostCommandPort {
    Post create(Post post);

    void delete(Post post);

    void deleteAll(List<Post> posts);
}
