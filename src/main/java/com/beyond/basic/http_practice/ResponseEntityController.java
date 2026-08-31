package com.beyond.basic.http_practice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

//ResponseEntity : http응답객체의 body뿐 아니라, 상태코드 및 헤더요소를 바꿔야 하는 경우에 사용
@RestController
@RequestMapping("/response_entity")
public class ResponseEntityController {
    
//    @ResponseStatus 어노테이션사용 : 상황에 따른 분기처리의 어려움
    @ResponseStatus(HttpStatus.CREATED)
    @GetMapping("/annotation")
    public String annotation(){
        return "ok";
    }
    
//    ResponsEntity 방식
    @GetMapping("/method1")
    public ResponseEntity<String> method1(){
        return new ResponseEntity<>("OK", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/method2")
    public ResponseEntity<?> method2(){
        return new ResponseEntity<>("OK", HttpStatus.NOT_FOUND);
    }

//    가장 추천하는 방식
//    ResponseEntity, ?(any), 빌더패턴을 사용하여 상태코드, header, body를 유연하게 생성
    @GetMapping("/method3")
    public ResponseEntity<?> method3(){
        Member1 dto = new Member1("hongildong", "hong@naver.com");
        return ResponseEntity
                .status(HttpStatus.CREATED)
//                .header("Content-Type", "application/json")
                .body(dto);
    }

    @GetMapping("/method4")
    public ResponseEntity<?> method4(){
        Member1 dto = new Member1("hongildong", "hong@naver.com");
        return ResponseEntity.ok(dto);
    }

}
