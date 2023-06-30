package com.ordo.oauth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ordo.oauth.domain.User;
import com.ordo.oauth.domain.dto.GoogleInfResponse;
import com.ordo.oauth.domain.dto.GoogleResponse;
import com.ordo.oauth.domain.dto.PostDto;
import com.ordo.oauth.domain.dto.returnDto;
import com.ordo.oauth.domain.entity.UserEntity;
import com.ordo.oauth.enums.RoleType;
import com.ordo.oauth.exception.AppException;
import com.ordo.oauth.exception.ErrorCode;
import com.ordo.oauth.querydsl.PostQueryRepository;
import com.ordo.oauth.repository.AlarmEntityRepository;
import com.ordo.oauth.repository.UserRepository;
import com.ordo.oauth.utils.JwtTokenUtils;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserServiceV2 {

  private final UserRepository userRepository;
  private final AlarmEntityRepository alarmEntityRepository;
  private final BCryptPasswordEncoder encoder;
  private final PostQueryRepository postQueryRepository;

  @Value("${jwt.token.secret}")
  private String secretKey;
//      private int expireTimeMs = 1000;
  private Long expireTimeMs = 1000 * 60 * 60L;

  public returnDto login(String userName, String password){
//        System.out.println("userName:::::" + userName);
//        System.out.println("password:::::" + password);
    //userName 없음
    User savedUser = loadUserByUsername(userName);

    //password 틀림
    if(!encoder.matches(password,savedUser.getPassword())){
      throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 잘못 되었습니다.");
    }

//        returnDto token = JwtTokenUtils.generateToken(savedUser.getUsername(), secretKey, expireTimeMs);

    returnDto token = JwtTokenUtils.generateToken(savedUser.getUsername(), secretKey, expireTimeMs);

    return token;
  }

  public UserEntity join(String userName, String password) {
    userRepository.findByUserName(userName).ifPresent(
        user -> {
          throw new AppException(ErrorCode.DUPLICATED_USER_NAME, " "+ userName +"는 이미 있습니다.");
        }
    );

    UserEntity user = UserEntity.builder().userName(userName).password(encoder.encode(password)).role(
        RoleType.USER).build();
    userRepository.save(user);

    return user;

  }


  public User loadUserByUsername(String userName) throws UsernameNotFoundException {
    UserEntity userEntity = userRepository.findByUserName(userName)
        .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+ "이 없습니다." ));

    User user = User.fromEntity(userEntity);

    return user;
  }

  public Optional<List<PostDto>> getMyselfPosts(Authentication auth){

    UserEntity user = userRepository.findByUserName(auth.getName()).orElseThrow(()->new NullPointerException("로그인이 필요한 동작입니다."));



    return postQueryRepository.getMyselfPosts(user.getId());
  }

  private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String GOOGLE_CLIENT_ID;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String GOOGLE_CLIENT_SECRET;

  @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
  private String GOOGLE_REDIRECT_URI;

  private final RestTemplate restTemplate = new RestTemplate();

  private final Environment env;

//  @Value("${oau}")
//  private String REDIRECT_URL;

  public String socialLogin(String code, String registrationId){
    RestTemplate restTemplate = new RestTemplate();
    Map<String, String> params = new HashMap<>();

    // 구글 서버에 보내는 내용
    params.put("code", code);
    params.put("client_id", GOOGLE_CLIENT_ID);
    params.put("client_secret", GOOGLE_CLIENT_SECRET);
    params.put("redirect_uri", GOOGLE_REDIRECT_URI);
    params.put("grant_type", "authorization_code");

    ResponseEntity<GoogleResponse> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, GoogleResponse.class);     // post로 구글서버에 요청

    String jwtToken = responseEntity.getBody().getId_token();
    Map<String, String> map=new HashMap<>();
    map.put("id_token",jwtToken);
    ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",
        map, GoogleInfResponse.class);
    String email=resultEntity2.getBody().getEmail();
    System.out.println("응답값 : " +email);

    // 응답 값으로 받은 데이터중 id_token은 jwt 토큰이다 jwt.io 페이지에서 RS256 디코딩하면 값이 보인다.

    Long findUser = userRepository.countByUserName(email);

    System.out.println("db검색 : " +findUser);
    String accessToken = JwtTokenUtils.generateAccessToken(email, secretKey, expireTimeMs);
    System.out.println("토큰 재생성 : " +accessToken);
    if(findUser != 0){
      return accessToken;
    }else{
      return email;
    }

  }








}
