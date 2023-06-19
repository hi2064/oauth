package com.ordo.oauth.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Transactional
@Service
public class LogoutService implements LogoutHandler {


  @Override
  public void logout(HttpServletRequest request, HttpServletResponse response, Authentication auth){
    if(auth != null){

    }
  }
}
