package com.ordo.oauth.domain.response;

import com.ordo.oauth.domain.dto.returnDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLoginResponse {
//    private returnDto jwt;
    private String jwt;
}
