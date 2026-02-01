package me.jaeyeop.blog.post.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QPost is a Querydsl query type for Post
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPost extends EntityPathBase<Post> {

    private static final long serialVersionUID = 572332447L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QPost post = new QPost("post");

    public final me.jaeyeop.blog.commons.persistence.QAbstractBaseEntity _super = new me.jaeyeop.blog.commons.persistence.QAbstractBaseEntity(this);

    public final me.jaeyeop.blog.user.domain.QUser author;

    public final ListPath<me.jaeyeop.blog.comment.domain.Comment, me.jaeyeop.blog.comment.domain.QComment> comments = this.<me.jaeyeop.blog.comment.domain.Comment, me.jaeyeop.blog.comment.domain.QComment>createList("comments", me.jaeyeop.blog.comment.domain.Comment.class, me.jaeyeop.blog.comment.domain.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    public final QPostInformation information;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public QPost(String variable) {
        this(Post.class, forVariable(variable), INITS);
    }

    public QPost(Path<? extends Post> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QPost(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QPost(PathMetadata metadata, PathInits inits) {
        this(Post.class, metadata, inits);
    }

    public QPost(Class<? extends Post> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.author = inits.isInitialized("author") ? new me.jaeyeop.blog.user.domain.QUser(forProperty("author"), inits.get("author")) : null;
        this.information = inits.isInitialized("information") ? new QPostInformation(forProperty("information")) : null;
    }

}

