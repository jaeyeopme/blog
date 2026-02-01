package me.jaeyeop.blog.user.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.dsl.StringTemplate;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.annotations.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserProfile is a Querydsl query type for UserProfile
 */
@SuppressWarnings("this-escape")
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QUserProfile extends BeanPath<UserProfile> {

    private static final long serialVersionUID = 1641114740L;

    public static final QUserProfile userProfile = new QUserProfile("userProfile");

    public final StringPath email = createString("email");

    public final StringPath introduce = createString("introduce");

    public final StringPath name = createString("name");

    public final StringPath picture = createString("picture");

    public QUserProfile(String variable) {
        super(UserProfile.class, forVariable(variable));
    }

    public QUserProfile(Path<? extends UserProfile> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserProfile(PathMetadata metadata) {
        super(UserProfile.class, metadata);
    }

}

