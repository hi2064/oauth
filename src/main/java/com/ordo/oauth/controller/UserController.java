package com.ordo.oauth.controller;

import com.ordo.oauth.domain.User;
import com.ordo.oauth.domain.dto.UserJoinRequest;
import com.ordo.oauth.domain.dto.UserLoginRequest;
import com.ordo.oauth.domain.dto.returnDto;
import com.ordo.oauth.domain.entity.UserEntity;
import com.ordo.oauth.domain.response.AlarmResponse;
import com.ordo.oauth.domain.response.Response;
import com.ordo.oauth.domain.response.UserJoinResponse;
import com.ordo.oauth.domain.response.UserLoginResponse;
import com.ordo.oauth.exception.AppException;
import com.ordo.oauth.exception.ErrorCode;
import com.ordo.oauth.service.UserService;
import com.ordo.oauth.utils.ClassUtils;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Api(tags = {"User Controller"})
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest dto){
        UserEntity userEntity = userService.join(dto.getUserName(), dto.getPassword());
        UserJoinResponse userJoinResponse = UserJoinResponse.builder()
                .userId(userEntity.getId())
                .userName(userEntity.getUserName())
                .build();
        return Response.success(userJoinResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<UserLoginResponse>> login(@RequestBody UserLoginRequest dto){
//        System.out.println("dto.getUserName():::::" + dto.getUserName());
//        System.out.println("dto.getPassword():::::" + dto.getPassword());
//        returnDto token = userService.login(dto.getUserName(), dto.getPassword());
        String token = userService.login(dto.getUserName(), dto.getPassword());
//        System.out.println("token" + token);

        return ResponseEntity.ok().body(Response.success(new UserLoginResponse(token)));
    }

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class)
            .orElseThrow(()-> new AppException(ErrorCode.INTERNAL_SERVER_ERROR));
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }
}