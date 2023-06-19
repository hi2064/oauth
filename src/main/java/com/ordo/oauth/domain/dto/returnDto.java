package com.ordo.oauth.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//@AllArgsConstructor
@NoArgsConstructor
@Getter
public class returnDto {

  String accessToken;

  String refreshToken;

  String username;

  @Builder
  public returnDto(String accessToken, String refreshToken, String username){
    this.accessToken = accessToken;
    this.refreshToken = refreshToken;
    this.username = username;
  }

}
