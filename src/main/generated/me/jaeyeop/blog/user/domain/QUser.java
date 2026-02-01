package me.jaeyeop.blog.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = -1170253963L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUser user = new QUser("user");

    public final me.jaeyeop.blog.commons.persistence.QAbstractBaseEntity _super = new me.jaeyeop.blog.commons.persistence.QAbstractBaseEntity(this);

    public final ListPath<me.jaeyeop.blog.comment.domain.Comment, me.jaeyeop.blog.comment.domain.QComment> comments = this.<me.jaeyeop.blog.comment.domain.Comment, me.jaeyeop.blog.comment.domain.QComment>createList("comments", me.jaeyeop.blog.comment.domain.Comment.class, me.jaeyeop.blog.comment.domain.QComment.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final NumberPath<Long> id = _super.id;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final ListPath<me.jaeyeop.blog.post.domain.Post, me.jaeyeop.blog.post.domain.QPost> posts = this.<me.jaeyeop.blog.post.domain.Post, me.jaeyeop.blog.post.domain.QPost>createList("posts", me.jaeyeop.blog.post.domain.Post.class, me.jaeyeop.blog.post.domain.QPost.class, PathInits.DIRECT2);

    public final QUserProfile profile;

    public final EnumPath<me.jaeyeop.blog.commons.authentication.OAuth2Provider> provider = createEnum("provider", me.jaeyeop.blog.commons.authentication.OAuth2Provider.class);

    public final SetPath<Role, EnumPath<Role>> roles = this.<Role, EnumPath<Role>>createSet("roles", Role.class, EnumPath.class, PathInits.DIRECT2);

    public QUser(String variable) {
        this(User.class, forVariable(variable), INITS);
    }

    public QUser(Path<? extends User> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUser(PathMetadata metadata, PathInits inits) {
        this(User.class, metadata, inits);
    }

    public QUser(Class<? extends User> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.profile = inits.isInitialized("profile") ? new QUserProfile(forProperty("profile")) : null;
    }

}

