package me.jaeyeop.blog.comment.adapter.out;

import static me.jaeyeop.blog.comment.domain.QComment.comment;
import static me.jaeyeop.blog.post.domain.QPost.post;
import static me.jaeyeop.blog.user.domain.QUser.user;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import me.jaeyeop.blog.comment.adapter.out.CommentResponse.Info;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

@Repository
public class CommentQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public CommentQueryRepository(final JPAQueryFactory jpaQueryFactory) {
    this.jpaQueryFactory = jpaQueryFactory;
  }

  public Page<Info> findInfoPageByPostId(final Long postId,
      final Pageable pageable) {
    return PageableExecutionUtils
        .getPage(getContent(postId, pageable), pageable, getTotalQuery(postId)::fetchOne);
  }

  private List<Info> getContent(final Long postId,
      final Pageable pageable) {
    final var commentInfo = new QCommentResponse_Info(
        comment.id,
        comment.content,
        user.name,
        comment.createdAt,
        comment.modifiedAt);

    return jpaQueryFactory.select(commentInfo)
        .from(comment)
        .innerJoin(comment.author, user)
        .innerJoin(comment.post, post)
        .where(post.id.eq(postId))
        .offset(pageable.getOffset())
        .limit(pageable.getPageSize())
        .fetch();
  }

  private JPAQuery<Long> getTotalQuery(final Long postId) {
    return jpaQueryFactory.select(comment.count())
        .from(comment)
        .innerJoin(comment.author, user)
        .innerJoin(comment.post, post)
        .where(post.id.eq(postId));
  }

}