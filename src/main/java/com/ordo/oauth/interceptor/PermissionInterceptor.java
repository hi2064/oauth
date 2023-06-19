package com.ordo.oauth.interceptor;

import com.ordo.oauth.annotation.Permission;
import com.ordo.oauth.domain.entity.UserEntity;
import com.ordo.oauth.enums.RoleType;
import com.ordo.oauth.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class PermissionInterceptor implements HandlerInterceptor {            // url 호출시 컨트롤러 진입전 가로채서 권한 확인하는 class

  private final UserRepository userRepository;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
    HandlerMethod method = (HandlerMethod)handler;
    Permission permission = method.getMethodAnnotation(Permission.class);           // 컨트롤러에서 어노테이션으로 지정해준 권한 가지고옴
    String controllerRole = String.valueOf(permission.authority());
    System.out.println("컨트롤러에서 준 권한 : "+controllerRole);

    if(permission == null){
      return true;
    }
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    UserEntity userEntity = userRepository.findAllByUserName(username);

    String role = String.valueOf(userEntity.getRole());
    System.out.println("접근한 유저 이름 : "+username);
    System.out.println("접근한 유저 권한 : "+role);
    if(controllerRole.equals(role)){
      System.out.println("사용자");
      return true;
    }else if(role.equals(RoleType.ADMIN.getValue())){
      System.out.println("관리자");
      return true;
    }else{
      System.out.println("권한 없음");
      return false;
    }
  }

//  @Override
//  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception{
//
//    int status = response.getStatus();
//
//
//  }

}
