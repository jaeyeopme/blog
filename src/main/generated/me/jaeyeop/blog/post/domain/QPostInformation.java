package me.jaeyeop.blog.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostInformation is a Querydsl query type for PostInformation
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QPostInformation extends BeanPath<PostInformation> {

    private static final long serialVersionUID = 1392450061L;

    public static final QPostInformation postInformation = new QPostInformation("postInformation");

    public final StringPath content = createString("content");

    public final StringPath coverImage = createString("coverImage");

    public final StringPath title = createString("title");

    public QPostInformation(String variable) {
        super(PostInformation.class, forVariable(variable));
    }

    public QPostInformation(Path<? extends PostInformation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostInformation(PathMetadata metadata) {
        super(PostInformation.class, metadata);
    }

}

