package com.ordo.oauth.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QPostEntityV2 is a Querydsl query type for PostEntityV2
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QPostEntityV2 extends EntityPathBase<PostEntityV2> {

    private static final long serialVersionUID = 62260714L;

    public static final QPostEntityV2 postEntityV2 = new QPostEntityV2("postEntityV2");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final StringPath body = createString("body");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final StringPath title = createString("title");

    public final NumberPath<Integer> userId = createNumber("userId", Integer.class);

    public QPostEntityV2(String variable) {
        super(PostEntityV2.class, forVariable(variable));
    }

    public QPostEntityV2(Path<? extends PostEntityV2> path) {
        super(path.getType(), path.getMetadata());
    }

    public QPostEntityV2(PathMetadata metadata) {
        super(PostEntityV2.class, metadata);
    }

}

