package com.ordo.oauth.controller;

import com.ordo.oauth.annotation.Permission;
import com.ordo.oauth.domain.User;
import com.ordo.oauth.domain.dto.CommentDto;
import com.ordo.oauth.domain.dto.CommentV2;
import com.ordo.oauth.domain.dto.PostCommentRequest;
import com.ordo.oauth.domain.dto.PostDto;
import com.ordo.oauth.domain.dto.PostDtoV2;
import com.ordo.oauth.domain.dto.PostRequest;
import com.ordo.oauth.domain.dto.PostRequestV2;
import com.ordo.oauth.domain.entity.CommentEntity;
import com.ordo.oauth.domain.entity.PostEntity;
import com.ordo.oauth.domain.entity.PostEntityV2;
import com.ordo.oauth.domain.response.CommentResponse;
import com.ordo.oauth.domain.response.Response;
import com.ordo.oauth.enums.RoleType;
import com.ordo.oauth.model.ApiResult;
import com.ordo.oauth.service.PostService;
import com.ordo.oauth.service.PostServiceV2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/posts")
@Api(tags = {"Post Controller V2"})
public class PostControllerV2 {

  private final PostService postService;

  private final PostServiceV2 postServiceV2;

  // 게시물 리스트
  @ApiOperation(value = "게시글 조회", notes = "전체 글목록을 조회한다.")
  @GetMapping
//  @Permission(authority = RoleType.USER)
  public ApiResult<Page<PostDto>> getPostList(@PageableDefault(size = 20)
  @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
//    Page<PostDto> postDtos = postService.getAllItems(pageable);

    
    return ApiResult.success(postService.getAllItemsV2(pageable));
//    return ApiResult.success(postServiceV2.posts())
  }

  @ApiOperation(value = "게시글 count" , notes = "전체 게시글 count")
  @GetMapping("/posts/count")
  public ApiResult<Long> getListCount(){
    return ApiResult.success(postServiceV2.getListCount());
  }

  // 게시물 리스트 테스트 api
  @ApiOperation(value = "게시글 조회 QueryDsl test", notes = "게시글 전체 조회 Querydsl test")
  @GetMapping("/test/posts")
  public ApiResult<List<PostDtoV2>> getPostListTest(){
    System.out.println("QueryDsl 테스트 컨트롤러");
    return ApiResult.success(postServiceV2.posts());
  }


  // 게시물 단건 검색
  @ApiOperation(value = "게시글 단건 검색", notes = "선택한 글의 상세정보를 조회한다.")
  @GetMapping("/{id}")
//  @Permission(authority = RoleType.USER)
  public ApiResult<PostDtoV2> getPost(@PathVariable Integer id){
    return ApiResult.success(postServiceV2.getPost(id));
  }

  // 좋아요 여부
  @ApiOperation(value = "글 좋아요 여부", notes = "선택한 글의 좋아요 여부를 확인한다")
  @GetMapping("/like/status/{postId}")
  @Permission(authority = RoleType.USER)
  public ApiResult<Integer> getLikeCount(@PathVariable Integer postId, Authentication auth){
    return ApiResult.success(postServiceV2.getLikeCount(postId, auth));
  }

  // 게시글 등록
  @ApiOperation(value = "글 등록", notes = "글을 등록한다.")
  @PostMapping("/create")
  @Permission(authority = RoleType.USER)
  public ApiResult<Integer> createPost(@RequestBody PostRequest request, Authentication auth){

    return ApiResult.success(postServiceV2.create(request, auth));
  }

//  @ApiOperation(value = "게시글 등록 이미지 등록", notes = "게시글을 등록하며 이미지도 등록")
//  @PostMapping("/create/image")
//  @Permission(authority = RoleType.USER)
//  public ApiResult<Integer> createImagePost(@RequestBody){
//
//  }

  // 게시글 수정
  @ApiOperation(value = "글 수정", notes = "선택한 글을 수정한다.")
  @PostMapping("/modify")
  @Permission(authority = RoleType.USER)
  public ApiResult<Integer> modifyPost(@RequestBody PostRequestV2 request, Authentication auth){

    return ApiResult.success(postServiceV2.modify(request, auth));
  }

  // 게시글 삭제
  @ApiOperation(value = "글 삭제", notes = "선택한 글을 삭제한다")
  @DeleteMapping("delete/{postId}")
  @Permission(authority = RoleType.USER)
  public ApiResult<Integer> deletePost(@PathVariable Integer postId, Authentication auth){
    return ApiResult.success(postServiceV2.delete(postId, auth));
  }

  @ApiOperation(value = "좋아요", notes = "글에 좋아요를 한다")
  @PostMapping("/like/{postId}")
  @Permission(authority = RoleType.USER)
  public ApiResult<String> likePost(@PathVariable Integer postId, Authentication auth){

    return ApiResult.success(postServiceV2.likePost(postId, auth));
  }

  @ApiOperation(value = "좋아요 취소", notes = "좋아요를 취소한다")
  @DeleteMapping("/like/cancel/{postId}")
  @Permission(authority = RoleType.USER)
  public ApiResult<String> likeCancelPost(@PathVariable Integer postId, Authentication auth){

    return ApiResult.success(postServiceV2.likeCancelPost(postId, auth));
  }


  @ApiOperation(value = "댓글조회", notes = "댓글을 조회한다")
  @GetMapping("/comment/{postId}")
  public ApiResult<Optional<List<CommentDto>>> getComment(@PathVariable Integer postId){
    return ApiResult.success(postServiceV2.getComment(postId));
  }

  // 댓글 생성
  @ApiOperation(value = "댓글 등록", notes = "글에 댓글을 등록한다.")
  @PostMapping("/create/comment/{postId}")
  @Permission(authority = RoleType.USER)
  public ApiResult<Integer> createComment(@PathVariable Integer postId, @RequestBody PostCommentRequest request, Authentication auth){

    return ApiResult.success(postServiceV2.createComment(postId, request,auth));
  }


  // 댓글 수정
  @ApiOperation(value = "댓글 수정", notes = "선택한 댓글을 수정한다.")
  @PostMapping("/modify/comment/{commentId}")
  @Permission(authority = RoleType.USER)
  public ApiResult<Integer> modifyComment(
      @PathVariable Integer commentId,
      @RequestBody CommentV2 dto,
      Authentication auth
  ){
    return ApiResult.success(postServiceV2.modifyComment(commentId, dto, auth));
  }

  // 댓글 삭제
  @ApiOperation(value = "댓글 삭제", notes = "선택한 댓글을 삭제한다.")
  @DeleteMapping("/delete/comment/{commentId}")
  @Permission(authority = RoleType.USER)
  public ApiResult<Integer> deleteComment(@PathVariable Integer commentId, Authentication auth){

    return ApiResult.success(postServiceV2.deleteComment(commentId, auth));
  }
}
