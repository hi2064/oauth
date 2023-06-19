package com.ordo.oauth.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PostResponse {
    private String message;
    private Integer postId;
}
