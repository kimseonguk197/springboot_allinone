package com.beyond.basic.p3_board.common.auth;

import com.beyond.basic.p3_board.author.domain.Author;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;


@Component
public class JwtTokenProvider {


//    중요정보의 경우 application.yml 저장. Value를 통해 주입.
    @Value("${jwt.secretKey}")
    private String secret_key_string;

    @Value("${jwt.expiration}")
    private int expiration;

//    인코딩된 문자열 -> 디코딩된 문자열 -> HS512알고리즘으로 암호화
    private Key signingKey;

//    생성자 호출 이후에 아래 메서드를 실행하게 함으로서, @Value시점보다 늦게 실행되게하여 값주입의 문제해결.
    @PostConstruct
    public void init(){
        signingKey = new SecretKeySpec(Base64.getDecoder().decode(secret_key_string), SignatureAlgorithm.HS512.getJcaName());
    }

    public String createToken(Author author){

        Date now = new Date();
        Claims claims = Jwts.claims()
                .setSubject(author.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration*60*1000L));//30분*60초*1000밀리초 : 밀리초형태로 변환
//          주된키값을 제외한 나머지 정보는 put사용하여 key:value세팅
//        claims.put("role", author.getRole().toString());

//        토큰의 구성요소 : 헤더, 페이로드, 시그니처(서명부)
        String token = Jwts.builder()
//                아래 3가지 요소는 페이로드
                .setClaims(claims)
//              secret키를 통해 서명값(signature) 생성
                .signWith(signingKey)
                .compact();
        return token;
    }


}
