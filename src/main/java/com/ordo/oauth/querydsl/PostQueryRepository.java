package com.ordo.oauth.querydsl;

import static com.ordo.oauth.domain.entity.QPostEntityV2.postEntityV2;
import static com.ordo.oauth.domain.entity.QCommentEntityV2.commentEntityV2;
import static com.ordo.oauth.domain.entity.QUserEntity.userEntity;
import static com.ordo.oauth.domain.entity.QLikeEntityV2.likeEntityV2;

import com.ordo.oauth.domain.dto.CommentDto;
import com.ordo.oauth.domain.dto.PostDto;
import com.ordo.oauth.domain.dto.PostDtoV2;
import com.ordo.oauth.domain.entity.PostEntityV2;
import com.ordo.oauth.domain.entity.UserEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;
import com.querydsl.core.types.Projections;

@RequiredArgsConstructor
@Repository
public class PostQueryRepository {

  private final JPAQueryFactory jpaQueryFactory;

  public List<PostDtoV2> getPost(){
    System.out.println("QueryDsl 테스트 쿼리문 진입");
    return jpaQueryFactory.select((Projections.fields(PostDtoV2.class,
        postEntityV2.id.as("id"),
        postEntityV2.title.as("title"),
        postEntityV2.body.as("body"),
        postEntityV2.createdAt.as("createdAt"),
        postEntityV2.lastModifiedAt.as("updatedAt")
//        commentEntityV2.comment.as("comment").
//        commentEntityV2.comment.as("comment")
//        postEntityV2.userId.as("userId")
    )))
        .from(postEntityV2)
//        .join(commentEntityV2)
//        .on(postEntityV2.id.eq(commentEntityV2.postId))
        .fetch();
//        .where()
  }

  public Optional<List<CommentDto>> getComment(Integer postId){
    return Optional.ofNullable(jpaQueryFactory.select((Projections.fields(CommentDto.class,
            commentEntityV2.id.as("id"),
            commentEntityV2.createdAt.as("createdAt"),
            commentEntityV2.lastModifiedAt.as("lastModifiedAt"),
            commentEntityV2.comment.as("comment"),
            commentEntityV2.postId.as("postId"),
            commentEntityV2.userId.as("userId"),
            userEntity.userName.as("userName"),
            userEntity.role.as("role")
        )))
        .from(commentEntityV2)
        .join(userEntity).on(commentEntityV2.userId.eq(userEntity.id))
        .where(commentEntityV2.postId.eq(postId))
        .fetch());
  }

  public Optional<List<PostDto>> getMyselfPosts(Integer userId){
    return Optional.ofNullable(jpaQueryFactory.select((Projections.fields(PostDto.class,
        postEntityV2.id.as("id"),
        postEntityV2.createdAt.as("createdAt"),
//        postEntityV2.lastModifiedAt.as("updatedAt"),
        postEntityV2.title.as("title"),
        postEntityV2.body.as("body"),
        userEntity.userName.as("userName")
        )))
        .from(postEntityV2)
        .join(likeEntityV2).on(postEntityV2.id.eq(likeEntityV2.postId))
        .join(userEntity).on(userEntity.id.eq(userId))
        .where(likeEntityV2.userId.eq(userId))
        .fetch());

  }

}
