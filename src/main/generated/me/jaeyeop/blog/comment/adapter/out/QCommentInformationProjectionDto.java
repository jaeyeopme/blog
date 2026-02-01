package me.jaeyeop.blog.comment.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.annotations.Generated;

/**
 * me.jaeyeop.blog.comment.adapter.out.QCommentInformationProjectionDto is a Querydsl Projection type for CommentInformationProjectionDto
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentInformationProjectionDto extends ConstructorExpression<CommentInformationProjectionDto> {

    private static final long serialVersionUID = 102900914L;

    public QCommentInformationProjectionDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> authorName, com.querydsl.core.types.Expression<? extends me.jaeyeop.blog.comment.domain.CommentInformation> information, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> modifiedAt) {
        super(CommentInformationProjectionDto.class, new Class<?>[]{long.class, String.class, me.jaeyeop.blog.comment.domain.CommentInformation.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, authorName, information, createdAt, modifiedAt);
    }

}

