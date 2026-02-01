package me.jaeyeop.blog.comment.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentInformation is a Querydsl query type for CommentInformation
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QCommentInformation extends BeanPath<CommentInformation> {

    private static final long serialVersionUID = -1908573147L;

    public static final QCommentInformation commentInformation = new QCommentInformation("commentInformation");

    public final StringPath content = createString("content");

    public QCommentInformation(String variable) {
        super(CommentInformation.class, forVariable(variable));
    }

    public QCommentInformation(Path<? extends CommentInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentInformation(PathMetadata metadata) {
        super(CommentInformation.class, metadata);
    }

}

