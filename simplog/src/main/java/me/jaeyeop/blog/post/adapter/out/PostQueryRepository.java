package me.jaeyeop.blog.post.adapter.out;

import static me.jaeyeop.blog.post.domain.QPost.post;
import static me.jaeyeop.blog.user.domain.QUser.user;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import me.jaeyeop.blog.post.adapter.out.response.PostInfo;
import me.jaeyeop.blog.post.adapter.out.response.QPostInfo;
import org.springframework.stereotype.Repository;

@Repository
public class PostQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public PostQueryRepository(final JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public Optional<PostInfo> findInfoById(final Long id) {
    return Optional.ofNullable(jpaQueryFactory.select(
            new QPostInfo(post.id,
                post.title,
                post.content,
                user.name,
                post.createdAt,
                post.modifiedAt))
        .from(post)
        .innerJoin(post.author, user)
        .where(post.id.eq(id))
        .fetchOne());
  }

}
