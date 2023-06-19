package com.ordo.oauth.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @Target({ElementType.PARAMITER}) == 파라미터에서 토큰값 추출



@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthToken {

}
