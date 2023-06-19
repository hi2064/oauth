package com.ordo.oauth.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QAlarmEntity is a Querydsl query type for AlarmEntity
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QAlarmEntity extends EntityPathBase<AlarmEntity> {

    private static final long serialVersionUID = 167004361L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QAlarmEntity alarmEntity = new QAlarmEntity("alarmEntity");

    public final QBaseEntity _super = new QBaseEntity(this);

    public final EnumPath<com.ordo.oauth.domain.AlarmType> alarmType = createEnum("alarmType", com.ordo.oauth.domain.AlarmType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> fromUserId = createNumber("fromUserId", Integer.class);

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedAt = _super.lastModifiedAt;

    public final NumberPath<Integer> targetId = createNumber("targetId", Integer.class);

    public final StringPath text = createString("text");

    public final QUserEntity user;

    public QAlarmEntity(String variable) {
        this(AlarmEntity.class, forVariable(variable), INITS);
    }

    public QAlarmEntity(Path<? extends AlarmEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QAlarmEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QAlarmEntity(PathMetadata metadata, PathInits inits) {
        this(AlarmEntity.class, metadata, inits);
    }

    public QAlarmEntity(Class<? extends AlarmEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new QUserEntity(forProperty("user")) : null;
    }

}

