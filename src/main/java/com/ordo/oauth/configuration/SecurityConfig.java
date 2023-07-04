package com.ordo.oauth.configuration;

import com.ordo.oauth.security.filter.JwtTokenFilter;
//import com.example.demo.configuration.filter.JwtTokenfilter;
import com.ordo.oauth.exception.CustomAuthenticationEntryPoint;
import com.ordo.oauth.security.filter.ExceptionHandlerFilter;
import com.ordo.oauth.service.LogoutService;
import com.ordo.oauth.service.UserService;
//import com.example.demo.utils.JwtTokenUtil;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    private final LogoutService logoutService;
    @Value("${jwt.token.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .httpBasic().disable()
                .csrf().disable()
//              .cors((cors)->cors.disable())
                .cors().and()
                .authorizeRequests()
//            .antMatchers("/**").permitAll()
                .antMatchers("/api/v1/users/join", "/api/v1/users/login", "/api/v2/users/login/**", "https://accounts.google.com/o/oauth2/auth", "https://oauth2.googleapis.com/token", "https://nid.naver.com/oauth2.0/authorize","https://nid.naver.com/oauth2.0/token", "https://openapi.naver.com/v1/nid/me", "https://kauth.kakao.com/**", "https://kapi.kakao.com/v2/user/me").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/posts/*", "/api/v1/posts").authenticated()
                .antMatchers(HttpMethod.GET,"/api/v1/hello/api-auth-test").authenticated()
                .and()
//                .formLogin((login)->login
                    // 로그인 페이지를 설정합니다. 무슨 말이냐면 로그인을 하지 않았을 경우 로그인이 필요한 페이지를 들어갔을 때 로그인 페이지로 다시
                    // redirect 하기 위한 메소드입니다.
                    // 즉, 인가되지 않은 사용자에게 보여줄 페이지를 설정합니다.
                    // 더불어 logout 기능 역시 /logout으로 컨트롤러 작성 없이 바로 이용할 수 있으며 logout후에는 loginPage에 설정된
                    // 경로의 페이지를 보여줍니다.
//                    .loginPage("http://consol.ordo.net:3000/login")
                    // formLogin의 자동 로그인 방식을 이용합니다. username과 password를 위의 경로에
                    // 던져주면 자동으로 로그인이 되도록 구현합니다.
                    // login 주소가 호출이 되면 시큐리티가 낚아채서 대신 로그인 진행함.
                    // controller에 login 안만들어줘도 됨.
//                    .defaultSuccessUrl("/")
                    // 로그인 성공 후 이동 페이지
//                    .loginProcessingUrl("/")
                    // 로그인 Form Action Url
//                    .successHandler()
                    // 로그인 성공 후 핸들러
//                )
            // 로그아웃
                .logout((logout)->logout.logoutUrl("/api/v1/users/logout").addLogoutHandler(((request, response, authentication) -> {
                    HttpSession session = request.getSession();
                    if(session != null){
                        session.invalidate();
                    }})).logoutSuccessUrl("/").deleteCookies().clearAuthentication(true).invalidateHttpSession(true).permitAll())
            // 로그아웃
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new CustomAuthenticationEntryPoint())
                .and()
                .addFilterBefore(new JwtTokenFilter(userService, secretKey), UsernamePasswordAuthenticationFilter.class)

                .addFilterBefore(new ExceptionHandlerFilter(), JwtTokenFilter.class)
                .build();
    }
}
