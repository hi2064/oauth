package com.ordo.oauth.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

  private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";      // 구글 토큰 요청 uri

  @Value("${spring.security.oauth2.client.registration.google.client-id}")
  private String GOOGLE_CLIENT_ID;

  @Value("${spring.security.oauth2.client.registration.google.client-secret}")
  private String GOOGLE_CLIENT_SECRET;

  @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
  private String GOOGLE_REDIRECT_URI;             // 구글 유저정보 요청 uri

  private final RestTemplate restTemplate = new RestTemplate();

  private final Environment env;

//  @Value("${oau}")
//  private String REDIRECT_URL;

  // 구글 로그인
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

    String jwtToken = responseEntity.getBody().getId_token();         // 구글서버에서 받은 access토큰
    Map<String, String> map=new HashMap<>();
    map.put("id_token",jwtToken);
    ResponseEntity<GoogleInfResponse> resultEntity2 = restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo",     // 구글 서버에 user info 요청
        map, GoogleInfResponse.class);
    String email=resultEntity2.getBody().getEmail();            // 구글 서버에서 user info email

    Long findUser = userRepository.countByUserName(email);      // 회원 테이블 select 해서 가입된 유저인지 판단



    if(findUser != 0){
      String accessToken = JwtTokenUtils.generateAccessToken(email, secretKey, expireTimeMs);     // jwt 토큰 재생성
      return accessToken;     // 가입되어있으니 토큰 리턴
    }else{
      return email;           // 가인 안되어있으니 email정보 리턴
    }

  }


  private String NAVER_TOKEN_URL = "https://nid.naver.com/oauth2.0/token";          // 네이버 토큰 요청 uri

//  @Value("${spring.security.oauth2.client.registration.naver.client-id}")
  private String NAVER_CLIENT_ID = "QbmV1JksUqN_pOAFxdDl";

//  @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
  private String NAVER_CLIENT_SECRET = "ZsjXLliWVQ";

//  @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
  private String NAVER_REDIRECT_URL = "http://localhost:8090/api/v2/users/login/naver";       // 네이버 유저 정보 요청 uri

  private String GRANT_TYPE = "authorization_code";

  // 네이버 프로필 조회
  private final static String PROFILE_API_URL = "https://openapi.naver.com/v1/nid/me";

  public String socialLoginNaver(String code, String registrationId){

    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www.form-urlencoded;charset=utf-8");           // 헤더 인코딩
    MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();

    // 네이버 서버에 보내는 정보
    accessTokenParams.add("grant_type", "authorization_code");
    accessTokenParams.add("client_id", NAVER_CLIENT_ID);
    accessTokenParams.add("client_secret", NAVER_CLIENT_SECRET);
    accessTokenParams.add("code" , code);	// 응답으로 받은 코드
    accessTokenParams.add("state" , ""); // 응답으로 받은 상태

    ResponseEntity<GoogleResponse> responseEntity = restTemplate.postForEntity(NAVER_TOKEN_URL, accessTokenParams, GoogleResponse.class);     // post로 네이버서버에 요청

    String AccessToken = "Bearer " + responseEntity.getBody().getAccess_token();      // Bearer 타입 토큰
    Map<String, String> map = new HashMap<>();
    map.put("authorization", "Bearer "+AccessToken);

    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);     // 헤더 인코딩
    header.set("authorization", AccessToken);                         // 헤더에 토큰 set

    HttpEntity<?> result = new HttpEntity<>(header);
    Map<String, Object> mmap = new HashMap<>();
    HttpEntity<Map> response = null;
    try{
      response = restTemplate.exchange(
          PROFILE_API_URL,
          HttpMethod.GET,
          result,
          Map.class
      );
    }catch (HttpStatusCodeException e){
      System.out.println("error :" + e);
    }

    mmap = response.getBody();
    String email = ((LinkedHashMap) mmap.get("response")).get("email").toString();
    System.out.println("네이버 서버에서 받은 이메일 정보 = "+email);
    Long findUser = userRepository.countByUserName(email);
    if(findUser != 0){
      System.out.println("회원");
      String accessToken = JwtTokenUtils.generateAccessToken(email, secretKey, expireTimeMs);     // jwt 토큰 재생성
      return accessToken;     // 가입되어있으니 토큰 리턴
    }else{
      System.out.println("비회원");
      return email;
    }
  }

  private String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";         // 카카오 토큰 요청 uri

  private String KAKAO_CLIENT_ID = "e68d6ac5bb54cbee735932488ec03caf";

  private String KAKAO_SECRET_KEY = "baeec60c75feca46df7dcfb955e3c676";

  private String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";       // 카카오 유저 정보 요청 uri


  public String socialLoginKakao(String code, String registrationId){
    RestTemplate restTemplate = new RestTemplate();
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", "application/x-www.form-urlencoded;charset=utf-8");           // 헤더 인코딩
    MultiValueMap<String, String> accessTokenParams = new LinkedMultiValueMap<>();
    accessTokenParams.add("client_id", KAKAO_CLIENT_ID);
    accessTokenParams.add("client_secret", KAKAO_SECRET_KEY);
    accessTokenParams.add("grant_type", "authorization_code");
    accessTokenParams.add("redirect_uri", "http://localhost:8090/api/v2/users/login/kakao");
    accessTokenParams.add("code", code);

    ResponseEntity<GoogleResponse> responseEntity = restTemplate.postForEntity(KAKAO_TOKEN_URL, accessTokenParams, GoogleResponse.class);     // post로 카카오서버에 요청

    String token = "Bearer "+responseEntity.getBody().getAccess_token();

    HttpHeaders header = new HttpHeaders();
    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    header.set("Authorization", token);

    HttpEntity<?> result = new HttpEntity<>(header);
    Map<String, Object> mmap = new HashMap<>();
    HttpEntity<Map> response = null;
    try{
      response = restTemplate.exchange(
          KAKAO_USER_INFO_URL,
          HttpMethod.GET,
          result,
          Map.class
      );
    }catch (HttpStatusCodeException e){
      System.out.println("error :" + e);
    }
    mmap = response.getBody();
    String email = ((LinkedHashMap) mmap.get("kakao_account")).get("email").toString();
    System.out.println("카카오 서버에서 받은 이메일 정보 = "+email);
    Long findUser = userRepository.countByUserName(email);

    if(findUser != 0){
      System.out.println("회원");
      String accessToken = JwtTokenUtils.generateAccessToken(email, secretKey, expireTimeMs);     // jwt 토큰 재생성
      return accessToken;     // 가입되어있으니 토큰 리턴
    }else{
      System.out.println("비회원");
      return email;
    }
  }





}
