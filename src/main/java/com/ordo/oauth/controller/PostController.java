package com.ordo.oauth.controller;

//import com.example.demo.domain.dto.*;
import com.ordo.oauth.annotation.AuthToken;
import com.ordo.oauth.annotation.Permission;
import com.ordo.oauth.domain.dto.CommentModifyRequest;
import com.ordo.oauth.domain.dto.ModifyRequest;
import com.ordo.oauth.domain.dto.PostCommentRequest;
import com.ordo.oauth.domain.dto.PostDto;
import com.ordo.oauth.domain.dto.PostRequest;
import com.ordo.oauth.domain.entity.CommentEntity;
import com.ordo.oauth.domain.entity.PostEntity;
import com.ordo.oauth.domain.response.CommentResponse;
import com.ordo.oauth.domain.response.CommentSimpleResponse;
import com.ordo.oauth.domain.response.PostResponse;
import com.ordo.oauth.domain.response.Response;
import com.ordo.oauth.enums.RoleType;
import com.ordo.oauth.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
@Api(tags = {"Post Controller"})
public class PostController {

    private final PostService postService;


    @PostMapping
    @Permission(authority = RoleType.USER)
    public Response<PostResponse> posts(@RequestBody PostRequest dto, Authentication authentication){

        SecurityContextHolder authority = (SecurityContextHolder) SecurityContextHolder.getContext().getAuthentication();
        System.out.println("인터셉터 권환 확인");
        System.out.println(authority);

        System.out.println("Controller Test Enter");
        PostDto postDto = postService.write(dto.getTitle(),dto.getBody(), authentication.getName());
        System.out.println("Controller Test");
        return Response.success(new PostResponse("포스트 등록 완료", postDto.getId()));
    }

    /* Post 1개 조회
     */
    @GetMapping("/detail/{id}")
    @Permission(authority = RoleType.USER)
    public ResponseEntity<Response<PostDto>> findById(@PathVariable Integer id) {
        PostDto postDto = postService.get(id);
        return ResponseEntity.ok().body(Response.success(postDto));
    }

    /* Post List 조회
     */

    @GetMapping
    @Permission(authority = RoleType.USER)
    public ResponseEntity<Response<Page<PostDto>>> getPostList(@PageableDefault(size = 20)
                                         @SortDefault (sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<PostDto> postDtos = postService.getAllItems(pageable);
        return ResponseEntity.ok().body(Response.success(postDtos));
    }

    @PutMapping("/{id}")    // postid → string으로만 오는 거 같은데 숫자형태로 올 수 없는지
    @Permission(authority = RoleType.USER)
//    @PostMapping("{id}")
    public ResponseEntity<Response<PostResponse>> modify(@PathVariable Integer id, @RequestBody ModifyRequest dto, Authentication authentication) {
        System.out.println("Modify Controller Tes1");

        PostEntity postEntity = postService.modify(authentication.getName(), id, dto.getTitle(), dto.getBody());

        System.out.println("Modify Controller Tes3");
        return ResponseEntity.ok(Response.success(new PostResponse("포스트 수정 완료", postEntity.getId())));
    }

    @DeleteMapping("/{postId}")
    @Permission(authority = RoleType.USER)
    public Response<PostResponse> delete(@PathVariable Integer postId, Authentication authentication) {
        System.out.println("Delete Controller Tes1");

        postService.delete(authentication.getName(), postId);
        return Response.success(new PostResponse("포스트 삭제 완료", postId));
    }


    // do Like
    @PostMapping("/{id}/likes")
    @Permission(authority = RoleType.USER)
    public Response<String> like(@PathVariable Integer id, Authentication authentication) {
        postService.like(id, authentication.getName());
        return Response.success("좋아요를 눌렀습니다.");
    }

    // get Likes
    @GetMapping("/{id}/likes")
    @Permission(authority = RoleType.USER)
    public Response<Long> likeCount(@PathVariable Integer id) {
        Long likeCnt = postService.likeCount(id);
        return Response.success(likeCnt);
    }

    @GetMapping("/my")
    @Permission(authority = RoleType.USER)
    public Response<Page<PostDto>> my(Pageable pageable, Authentication authentication) {
        Page<PostDto> my = postService.my(authentication.getName(),pageable);
        return Response.success(my);
    }

    @PostMapping("/{id}/comments")
    @Permission(authority = RoleType.USER)
    public Response<CommentResponse> comment(@PathVariable Integer id, @RequestBody PostCommentRequest request, Authentication authentication) {
        CommentEntity commentEntity = postService.comment(id, authentication.getName(), request.getComment());
        CommentResponse commentResponse = CommentResponse.fromComment(commentEntity);
        return Response.success(commentResponse);
    }

    @GetMapping("/{id}/comments")
    @Permission(authority = RoleType.USER)
    public Response<Page<CommentResponse>> comment(@PathVariable Integer id, @PageableDefault(size = 10)
    @SortDefault (sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable, Authentication authentication) {
        Page<CommentResponse> commentResponses = postService.getComments(id, pageable)
                .map(commentEntity -> CommentResponse.fromComment(commentEntity) );

        return Response.success(commentResponses);
    }

    @PutMapping("/{postId}/comments/{id}")
    @Permission(authority = RoleType.USER)
    public Response<CommentResponse> modifyComment(@PathVariable Integer id, @RequestBody CommentModifyRequest dto, Authentication authentication) {
        CommentEntity commentEntity = postService.modifyComment(authentication.getName(), id, dto.getComment());
        return Response.success(CommentResponse.fromComment(commentEntity));
    }

    @DeleteMapping("/{postId}/comments/{id}")
    @Permission(authority = RoleType.USER)
    public Response<CommentSimpleResponse> deleteComment(@PathVariable Integer id, Authentication authentication) {

        postService.deleteComment(authentication.getName(), id);

        return Response.success(new CommentSimpleResponse("댓글 삭제 완료", id));
    }
}



