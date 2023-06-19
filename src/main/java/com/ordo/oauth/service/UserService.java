package com.ordo.oauth.service;


//import com.example.demo.configuration.filter.JwtTokenfilter;
import com.ordo.oauth.domain.User;
import com.ordo.oauth.domain.dto.returnDto;
import com.ordo.oauth.domain.entity.AlarmEntity;
import com.ordo.oauth.domain.entity.UserEntity;
import com.ordo.oauth.exception.AppException;
import com.ordo.oauth.exception.ErrorCode;
import com.ordo.oauth.repository.AlarmEntityRepository;
import com.ordo.oauth.repository.UserRepository;
import com.ordo.oauth.utils.JwtTokenUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Getter
@Setter
@RequiredArgsConstructor
public class UserService {

    //private final JwtTokenUtils jwtTokenUtil;
    private final UserRepository userRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final BCryptPasswordEncoder encoder;
    @Value("${jwt.token.secret}")
    private String secretKey;
//    private Long expireTimeMs = 1000 * 60 *60l;
    private Long expireTimeMs = 1000 * 60 * 60L;


    public User  loadUserByUsername(String userName) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+ "이 없습니다." ));

        User user = User.fromEntity(userEntity);

        return user;
    }

    public UserEntity join(String userName, String password){
        userRepository.findByUserName(userName).ifPresent(
                user -> {
                    throw new AppException(ErrorCode.DUPLICATED_USER_NAME, " "+ userName +"는 이미 있습니다.");
                }
        );
        UserEntity user = UserEntity.of(userName, encoder.encode(password));

        UserEntity savedUser = userRepository.save(user);

        return savedUser;
//        return "회원가입 성공";
    }

//    public returnDto login(String userName, String password){
    public String login(String userName, String password){
//        System.out.println("userName:::::" + userName);
//        System.out.println("password:::::" + password);
        //userName 없음
        User savedUser = loadUserByUsername(userName);

        //password 틀림
        if(!encoder.matches(password,savedUser.getPassword())){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 잘못 되었습니다.");
        }

//        returnDto token = JwtTokenUtils.generateToken(savedUser.getUsername(), secretKey, expireTimeMs);

String token = JwtTokenUtils.generateAccessToken(savedUser.getUsername(), secretKey, expireTimeMs);

        return token;
    }

    public Page<AlarmEntity> alarmList(Integer userId, Pageable pageable) {
        return alarmEntityRepository.findAllByUserId(userId, pageable);
    }
}

