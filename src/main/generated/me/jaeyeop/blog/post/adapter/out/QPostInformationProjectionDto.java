package me.jaeyeop.blog.post.adapter.out;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.annotations.Generated;

/**
 * me.jaeyeop.blog.post.adapter.out.QPostInformationProjectionDto is a Querydsl Projection type for PostInformationProjectionDto
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QPostInformationProjectionDto extends ConstructorExpression<PostInformationProjectionDto> {

    private static final long serialVersionUID = 916424622L;

    public QPostInformationProjectionDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<? extends me.jaeyeop.blog.post.domain.PostInformation> information, com.querydsl.core.types.Expression<String> authorName, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> lastModifiedAt) {
        super(PostInformationProjectionDto.class, new Class<?>[]{long.class, me.jaeyeop.blog.post.domain.PostInformation.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, information, authorName, createdAt, lastModifiedAt);
    }

}

