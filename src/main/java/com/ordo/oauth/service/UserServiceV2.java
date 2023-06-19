package com.ordo.oauth.service;

import com.ordo.oauth.domain.User;
import com.ordo.oauth.domain.dto.returnDto;
import com.ordo.oauth.domain.entity.UserEntity;
import com.ordo.oauth.enums.RoleType;
import com.ordo.oauth.exception.AppException;
import com.ordo.oauth.exception.ErrorCode;
import com.ordo.oauth.repository.AlarmEntityRepository;
import com.ordo.oauth.repository.UserRepository;
import com.ordo.oauth.utils.JwtTokenUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserServiceV2 {

  private final UserRepository userRepository;
  private final AlarmEntityRepository alarmEntityRepository;
  private final BCryptPasswordEncoder encoder;
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
}
