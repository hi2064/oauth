package com.ordo.oauth.controller;

import com.ordo.oauth.annotation.AuthToken;
import com.ordo.oauth.annotation.Permission;
import com.ordo.oauth.domain.dto.PostDto;
import com.ordo.oauth.domain.dto.UserJoinRequest;
import com.ordo.oauth.domain.dto.UserLoginRequest;
import com.ordo.oauth.domain.dto.returnDto;
import com.ordo.oauth.domain.entity.UserEntity;
import com.ordo.oauth.domain.response.Response;
import com.ordo.oauth.domain.response.UserLoginResponse;
import com.ordo.oauth.domain.response.UserLoginResponseV2;
import com.ordo.oauth.enums.RoleType;
import com.ordo.oauth.model.ApiResult;
import com.ordo.oauth.repository.UserRepository;
import com.ordo.oauth.service.UserService;
import com.ordo.oauth.service.UserServiceV2;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/users")
@Api(tags = {"User Controller V2"})
public class UserControllerV2 {

  private UserRepository userRepository;

  private final UserServiceV2 userServiceV2;

  @ApiOperation(value = "로그인", notes = "로그인 처리를 한다.")
  @PostMapping("/login")
  public ApiResult<Response<UserLoginResponseV2>> login(@RequestBody UserLoginRequest dto) {
//        System.out.println("dto.getUserName():::::" + dto.getUserName());
//        System.out.println("dto.getPassword():::::" + dto.getPassword());
    returnDto token = userServiceV2.login(dto.getUserName(), dto.getPassword());
//    String token = userService.login(dto.getUserName(), dto.getPassword());
//        System.out.println("token" + token);

    return ApiResult.success(Response.success(new UserLoginResponseV2(token)));
  }

  @ApiOperation(value = "회원가입", notes = "회원가입 처리를 한다.")
  @PostMapping("/join")
  public ApiResult<UserEntity> join(@RequestBody UserJoinRequest request) {

    UserEntity user = userServiceV2.join(request.getUserName(), request.getPassword());

    return ApiResult.success(user);
  }

  @ApiOperation(value = "회원 로그아웃", notes = "해당 회원을 로그아웃 처리한다")
  @PostMapping("/logout")
  public ApiResult<Void> getLogout(HttpServletRequest request, HttpServletResponse response) {

    System.out.println("토큰 : " + request.getHeader(HttpHeaders.AUTHORIZATION));
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    System.out.println("권한 정보 : " + auth);
    if (auth != null) {
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }

    return ApiResult.success(null);
  }


  @ApiOperation(value = "토큰 재발급", notes = "토큰을 재발급 한다")
  @PostMapping("/reissue")
  public ApiResult<Response<UserLoginResponse>> reissue(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String getToken) {

//    final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

    System.out.println("가져온 토큰 : " + getToken);

    return ApiResult.success(Response.success(new UserLoginResponse(getToken)));
  }


  @ApiOperation(value = "좋아요 한글", notes = "자신이 좋아요 한글을 가져온다")
  @GetMapping("/myself/posts")
  @Permission(authority = RoleType.USER)
  public ApiResult<Optional<List<PostDto>>> getMyselfPosts(Authentication auth) {

    return ApiResult.success(userServiceV2.getMyselfPosts(auth));
  }

  @ApiOperation(value = "구글 로그인", notes = "구글 로그인")
  @GetMapping("/login/{registrationId}")
  public String naverLogin(@RequestParam String code, @PathVariable String registrationId) {

    System.out.println("구글 로그인 들어옴");
    return userServiceV2.socialLogin(code, registrationId);

  }

}
