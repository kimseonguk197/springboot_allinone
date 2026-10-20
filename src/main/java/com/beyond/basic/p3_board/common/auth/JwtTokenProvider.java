package com.beyond.basic.p3_board.common.auth;

import com.beyond.basic.p3_board.author.domain.Author;
import com.beyond.basic.p3_board.author.repository.AuthorRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Component
public class JwtTokenProvider {


//    중요정보의 경우 application.yml 저장. Value를 통해 주입.
    @Value("${jwt.secretKey}")
    private String secret_key_string;

    @Value("${jwt.expiration}")
    private int expiration;
    @Value("${jwt.expirationRt}")
    private int expirationRt;

//    인코딩된 문자열 -> 디코딩된 문자열 -> HS512알고리즘으로 암호화
    private Key signingKey;

    private final RedisTemplate<String, String> redisTemplate;
    private final AuthorRepository authorRepository;

    public JwtTokenProvider(RedisTemplate<String, String> redisTemplate, AuthorRepository authorRepository) {
        this.redisTemplate = redisTemplate;
        this.authorRepository = authorRepository;
    }

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
                .setExpiration(new Date(now.getTime() + expirationRt*60*1000L));//30분*60초*1000밀리초 : 밀리초형태로 변환
//          주된키값을 제외한 나머지 정보는 put사용하여 key:value세팅
        claims.put("role", author.getRole().toString());

//        토큰의 구성요소 : 헤더, 페이로드, 시그니처(서명부)
        String token = Jwts.builder()
//                아래 3가지 요소는 페이로드
                .setClaims(claims)
//              secret키를 통해 서명값(signature) 생성
                .signWith(signingKey)
                .compact();
        return token;
    }

    public String createTokenRt(Author author){

        Date now = new Date();
        Claims claims = Jwts.claims()
                .setSubject(author.getEmail())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration*60*1000L));//30분*60초*1000밀리초 : 밀리초형태로 변환
//          주된키값을 제외한 나머지 정보는 put사용하여 key:value세팅
        claims.put("role", author.getRole().toString());

//        토큰의 구성요소 : 헤더, 페이로드, 시그니처(서명부)
        String token = Jwts.builder()
//                아래 3가지 요소는 페이로드
                .setClaims(claims)
//              secret키를 통해 서명값(signature) 생성
                .signWith(signingKey)
                .compact();

        redisTemplate.opsForValue().set(author.getEmail(), token, expirationRt, TimeUnit.MINUTES);
        return token;
    }

    public Author validateRt(String refreshToken){
        Claims claims = null;
//        rt토큰 그 자체를 검증
        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build().parseClaimsJws(refreshToken).getBody();
        }catch (Exception e){
            throw new IllegalArgumentException("잘못된 refresh 토큰입니다.");
        }

        String email = claims.getSubject();
        Author author = authorRepository.findByEmail(email).orElseThrow(()->new EntityNotFoundException("entity is not found"));
//        redis rt와 비교 검증
        String redisRt = redisTemplate.opsForValue().get(email);
        if(!redisRt.equals(refreshToken)){
            throw new IllegalArgumentException("잘못된 토큰입니다.");
        }
        return author;
    }
}
