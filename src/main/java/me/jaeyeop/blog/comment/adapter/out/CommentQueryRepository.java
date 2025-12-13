package me.jaeyeop.blog.comment.adapter.out;

import static me.jaeyeop.blog.comment.domain.QComment.comment;
import static me.jaeyeop.blog.post.domain.QPost.post;
import static me.jaeyeop.blog.user.domain.QUser.user;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

/**
 * @author jaeyeopme Created on 10/19/2022.
 */
@Repository
public class CommentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public CommentQueryRepository(final JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Page<CommentInformationProjectionDto> findInformationPageByPostId(
            final Long postId, final Pageable pageable) {
        final var content = getContent(postId, pageable);

        return PageableExecutionUtils.getPage(content, pageable, getTotalQuery(postId)::fetchOne);
    }

    public Optional<CommentInformationProjectionDto> findInformationById(final Long id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .select(getQCommentInformation())
                        .from(comment)
                        .innerJoin(comment.author, user)
                        .where(comment.id.eq(id))
                        .fetchOne());
    }

    private List<CommentInformationProjectionDto> getContent(
            final Long postId, final Pageable pageable) {
        return jpaQueryFactory
                .select(getQCommentInformation())
                .from(comment)
                .innerJoin(comment.post, post)
                .innerJoin(comment.author, user)
                .where(post.id.eq(postId))
                .orderBy(comment.createdAt.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private QCommentInformationProjectionDto getQCommentInformation() {
        return new QCommentInformationProjectionDto(
                comment.id,
                user.profile.name,
                comment.information,
                comment.createdAt,
                comment.lastModifiedAt);
    }

    private JPAQuery<Long> getTotalQuery(final Long postId) {
        return jpaQueryFactory
                .select(comment.count())
                .from(comment)
                .innerJoin(comment.author, user)
                .innerJoin(comment.post, post)
                .where(post.id.eq(postId));
    }
}
