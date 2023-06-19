package com.ordo.oauth.service;

import com.ordo.oauth.domain.dto.CommentDto;
import com.ordo.oauth.domain.dto.CommentV2;
import com.ordo.oauth.domain.dto.PostCommentRequest;
import com.ordo.oauth.domain.dto.PostDto;
import com.ordo.oauth.domain.dto.PostDtoV2;
import com.ordo.oauth.domain.dto.PostRequest;
import com.ordo.oauth.domain.dto.PostRequestV2;
import com.ordo.oauth.domain.entity.CommentEntity;
import com.ordo.oauth.domain.entity.CommentEntityV2;
import com.ordo.oauth.domain.entity.PostEntity;
import com.ordo.oauth.domain.entity.PostEntityV2;
import com.ordo.oauth.domain.entity.UserEntity;
import com.ordo.oauth.domain.response.CommentResponse;
import com.ordo.oauth.model.ApiResult;
import com.ordo.oauth.querydsl.PostQueryRepository;
import com.ordo.oauth.repository.CommentEntityRepository;
import com.ordo.oauth.repository.CommentEntityRepositoryV2;
import com.ordo.oauth.repository.PostRepository;
import com.ordo.oauth.repository.PostRepositoryV2;
import com.ordo.oauth.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostServiceImplV2 implements PostServiceV2{

  private final PostRepositoryV2 postRepositoryV2;
  private final UserRepository userRepository;
  private final CommentEntityRepository commentEntityRepository;
  private final CommentEntityRepositoryV2 commentEntityRepositoryV2;

  private final PostQueryRepository postQueryRepository;

//  private final PostQueryRepository postQueryRepository;

  @Transactional
  @Override
  public List<PostDtoV2> posts(){
    System.out.println("QueryDsl 테스트 임플");
    return postQueryRepository.getPost();
  }


  @Transactional
  @Override
  public PostDtoV2 getPost(Integer id){
    PostEntityV2 dto = postRepositoryV2.findById(id).orElseThrow(()->new NullPointerException("없는 글입니다."));

//    Optional<List<CommentEntityV2>> comment = commentEntityRepositoryV2.findByPostId(dto.getId()); // userName 없는 버전 entity조회

    Optional<List<CommentDto>> comment = postQueryRepository.getComment(dto.getId());       // queryDsl Version

    PostDtoV2 dtoV2 = PostDtoV2.builder()
        .id(dto.getId())
        .createdAt(dto.getCreatedAt())
        .updatedAt(dto.getLastModifiedAt())
        .title(dto.getTitle())
        .body(dto.getBody())
        .comment(comment)
        .build();
    return dtoV2;
  }

  @Transactional
  @Override
  public Integer create(PostRequest request, Authentication auth){
    UserEntity user = userRepository.findByUserName(auth.getName()).orElseThrow(()->new NullPointerException("로그인이 필요한 동작입니다."));

    PostEntityV2 postEntityV2 = PostEntityV2.builder().title(request.getTitle()).body(request.getBody()).userId(user.getId()).build();

    postRepositoryV2.save(postEntityV2);

    return user.getId();
  }

  @Transactional
  @Override
  public Integer modify(Integer userId, PostRequestV2 request) {

    UserEntity userEntity = userRepository.findById(userId).orElseThrow(()->new NullPointerException("없는 사용자 입니다."));

    PostEntityV2 postEntityV2 = postRepositoryV2.findByIdAndAndUserId(request.getId(), userEntity.getId()).orElseThrow(()-> new NullPointerException("존재하지 않는 글입니다."));

    System.out.println("유저 테이블에서 가져온 Id값 : "+userEntity.getId());
    System.out.println("포스트 테이블에서 가져온 Id 값 :"+postEntityV2.getUserId());

    postEntityV2.update(request.getTitle(), request.getBody());

    return userEntity.getId();
  }

  // 글 삭제
  @Transactional
  @Override
  public Integer delete(Integer postId, Authentication auth){
    UserEntity user = userRepository.findByUserName(auth.getName()).orElseThrow(()->new NullPointerException("로그인이 필요한 동작입니다."));
    System.out.println(postId);
    System.out.println(user.getId());
    System.out.println("123");
    PostEntityV2 dto = postRepositoryV2.findByIdAndAndUserId(postId, user.getId()).orElseThrow(()->new NullPointerException("본인 글만 지울수 있습니다."));
    System.out.println("123");
    postRepositoryV2.deleteById(dto.getId());
    return dto.getId();
  }

  // 댓글 생성
  @Transactional
  @Override
  public Integer createComment(Integer postId, PostCommentRequest request, Authentication auth){
    PostEntityV2 dto = postRepositoryV2.findById(postId).orElseThrow(()->new NullPointerException("없는 글입니다."));
    UserEntity user = userRepository.findByUserName(auth.getName()).orElseThrow(()->new NullPointerException("로그인이 필요한 동작입니다."));
    CommentEntityV2 comment = CommentEntityV2.builder().comment(request.getComment()).postId(dto.getId()).userId(user.getId()).build();
    commentEntityRepositoryV2.save(comment);
    return comment.getId();
  }

  // 댓글 수정
  @Transactional
  @Override
  public Integer modifyComment(Integer commentId, CommentV2 dto){
    CommentEntityV2 comment = commentEntityRepositoryV2.findById(commentId).orElseThrow(()->new NullPointerException("삭제된 댓글입니다."));
    PostEntityV2 post = postRepositoryV2.findById(comment.getPostId()).orElseThrow(()->new NullPointerException("없는 글입니다."));
    comment.update(dto.getComment());
    return comment.getId();
  }

  // 댓글 삭제
  @Transactional
  @Override
  public Integer deleteComment(Integer commentId){
    CommentEntityV2 comment = commentEntityRepositoryV2.findById(commentId).orElseThrow(()->new NullPointerException("삭제된 댓글입니다."));
    PostEntityV2 dto = postRepositoryV2.findById(comment.getPostId()).orElseThrow(()->new NullPointerException("없는 글입니다."));
    commentEntityRepositoryV2.deleteById(comment.getId());
    return comment.getId();
  }

}
