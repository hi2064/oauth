package com.ordo.oauth.configuration;

import com.ordo.oauth.interceptor.PermissionInterceptor;
import com.ordo.oauth.repository.UserRepository;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebRoleConfiguration implements WebMvcConfigurer {

    private final UserRepository userRepository;

    public WebRoleConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(new PermissionInterceptor(userRepository))                  // 인터셉터 등록
            .addPathPatterns("/**")                                                         // 해당 경로에 접근하기 전에 인터셉터가 가로챈다
            .excludePathPatterns("/api/v1/users/*", "/**");                                        // 해당 경로는 인터셉터가 가로채지 않는다
    }
}
