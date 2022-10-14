package me.jaeyeop.blog.post.adapter.out;

import static me.jaeyeop.blog.post.domain.QPost.post;
import static me.jaeyeop.blog.user.domain.QUser.user;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import me.jaeyeop.blog.post.adapter.in.response.PostInformation;
import me.jaeyeop.blog.post.adapter.in.response.QPostInformation;
import org.springframework.stereotype.Repository;

@Repository
public class PostQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public PostQueryRepository(final JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public Optional<PostInformation> getPostInformationById(final Long id) {
    final QPostInformation select = new QPostInformation(
        post.title,
        post.content,
        post.createdAt,
        post.modifiedAt,
        user.name);

    return Optional.ofNullable(jpaQueryFactory.select(select)
        .from(post)
        .innerJoin(post.author, user)
        .where(post.id.eq(id))
        .fetchOne());
  }

}
