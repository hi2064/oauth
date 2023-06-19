package com.ordo.oauth.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QCommentEntityV2 is a Querydsl query type for CommentEntityV2
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCommentEntityV2 extends EntityPathBase<CommentEntityV2> {

    private static final long serialVersionUID = 978461171L;

    public static final QCommentEntityV2 commentEntityV2 = new QCommentEntityV2("commentEntityV2");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath comment = createString("comment");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> deletedAt = createDateTime("deletedAt", java.time.LocalDateTime.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final NumberPath<Integer> postId = createNumber("postId", Integer.class);

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QCommentEntityV2(String variable) {
        super(CommentEntityV2.class, forVariable(variable));
    }

    public QCommentEntityV2(Path<? extends CommentEntityV2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QCommentEntityV2(PathMetadata metadata) {
        super(CommentEntityV2.class, metadata);
    }

}

