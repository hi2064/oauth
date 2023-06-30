package com.ordo.oauth.utils;

import com.ordo.oauth.domain.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import com.ordo.oauth.domain.dto.returnDto;

import java.util.Date;


public class JwtTokenUtils {

    public static Boolean validate(String token, String userName, String key) {
        String usernameByToken = getUsername(token, key);
        return usernameByToken.equals(userName) && !isTokenExpired(token, key);
    }

    public static Claims extractAllClaims(String token, String key) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
    }

    public static String getUsername(String token, String key) {
        return extractAllClaims(token, key).get("username", String.class);
    }

    public static String getEmail(String token, String key) {
        return extractAllClaims(token, key).get("email", String.class);
    }

    public static Boolean isTokenExpired(String token, String key) {
        Date expiration = extractAllClaims(token, key).getExpiration();
        return expiration.before(new Date());
    }

    public static String generateAccessToken(String username, String key, long expiredTimeMs) {

        return doGenerateToken(username, expiredTimeMs, key);
    }

    public static returnDto generateToken(String username, String key, long expiredTimeMs){


        String accessToken = doGenerateToken(username, expiredTimeMs, key);
//        String accessToken = doGenerateToken(username, 100, key);
        System.out.println("accessToken : "+accessToken);
        String refreshToken = createRefreshToken(username, key);
        System.out.println("refreshToken : "+refreshToken);
//        returnDto token = returnDto.builder().accessToken(accessToken).refreshToken(refreshToken).username(username).build();

        returnDto token = new returnDto(accessToken, refreshToken, username);

        System.out.println("return accessToken 값 : "+token.getAccessToken());
        System.out.println("return refreshToken 값 : "+token.getRefreshToken());
        System.out.println("return userName 값 : "+token.getUsername());
        return token;
    }

    private static String doGenerateToken(String username, long expireTime, String key) {
        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    private static String createRefreshToken(String username, String key){

        Claims claims = Jwts.claims();
        claims.put("username", username);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis()+1000 * 600 * 60L))
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
    }
}
