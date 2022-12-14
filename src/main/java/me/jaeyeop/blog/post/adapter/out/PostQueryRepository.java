package me.jaeyeop.blog.post.adapter.out;

import static me.jaeyeop.blog.post.domain.QPost.post;
import static me.jaeyeop.blog.user.domain.QUser.user;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import org.springframework.stereotype.Repository;

/**
 * @author jaeyeopme Created on 10/12/2022.
 */
@Repository
public class PostQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public PostQueryRepository(final JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public Optional<PostInformationProjectionDto> findInfoById(final Long postId) {
    final var postInfo = new QPostInformationProjectionDto(
        post.id,
        post.information,
        user.profile.name,
        post.createdAt,
        post.lastModifiedAt);

    return Optional.ofNullable(
        jpaQueryFactory.select(postInfo)
            .from(post)
            .innerJoin(post.author, user)
            .where(post.id.eq(postId))
            .fetchOne()
    );
  }

}
