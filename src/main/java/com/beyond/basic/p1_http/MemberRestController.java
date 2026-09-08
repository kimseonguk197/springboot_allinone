package com.beyond.basic.p1_http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/member")
public class MemberRestController {
    @GetMapping("/text-data2")
    public static String textDataReturn(){
        return "hongildong2";
    }
//    get요청의 url의 2가지 패턴 : pathvariable, 쿼리파라미터
//    case1. pathvariable방식을 통해 사용자로부터 url에서 데이터 추출
//    데이터의 형식 : /member/path/1
    @GetMapping("/path/{inputId}")
    public String path(@PathVariable Long inputId){
//        별도의 형변환 없이도, 원하는 자료형으로 형변환되어 매개변수로 주입.(매개변수의 변수명이 url의변수명과 일치해야함)
        System.out.println(inputId);
        return "ok";
    }
    
//    case2.parameter방식을 통한 url에서의 데이터 추출(주로, 검색, 정렬 요청등의 상황에서 사용)
//    case2-1)1개의 파라미터에서 데이터 추출
//    데이터형식 : member/param1?name=hongildong
    @GetMapping("/param1")
    public String param1(@RequestParam(value = "name") String inputName){
        System.out.println(inputName);
        return "OK";
    }
//    case2-2)2개의 파라미터에서 데이터 추출
//    데이터형식 : member/param2?name=hongildong&email=hong@naver.com
    @GetMapping("/param2")
    public String param2(@RequestParam(value = "name") String inputName,
                         @RequestParam(value = "email") String inputEmail){
        System.out.println(inputName);
        System.out.println(inputEmail);
        return "OK";
    }

//    case3)파라미터의 개수가 많아질경우, ModelAttribute를 통한 데이터바인딩
//    데이터바인딩은 param의 데이터를 모아 객체로 자동 매핑 및 생성
//    이때 반드시 바인딩 할 객체에는
//    데이터형식 : member/param3?name=hongildong&email=hong@naver.com
    @GetMapping("/param3")
    public String param3(@ModelAttribute Member1 member1){ //    @ModelAttribute는 생략가능.
        System.out.println(member1);
        return "OK";
    }

//    post요청 처리 case : urlencoded, multipart-formdata, json
//    case1. body의 content-type이 urlencoded형식
//    형식 : body부에 name=hongildong&email=hong@naver.com
    @PostMapping("/url-encoded")
//    형식이 url의 파라미터방식과 동일하므로, RequestParam 또는 데이터바인딩 가능
    public String urlEncoded(@ModelAttribute Member1 member1){
        System.out.println(member1);
        return "ok";
    }

//    case2. body의 content-type이 multipart-formdata
//    case2-1)1개의 이미지만 있는 경우
//    형식 : body부에 name=hongildong&email=hong@naver.com&profileImage=xxxx
    @PostMapping("/multipart-formdata")
    public String multipartFormdata(@ModelAttribute Member1 member1, @RequestParam(value = "profileImage")MultipartFile profileImage){
        System.out.println(member1);
        System.out.println(profileImage.getOriginalFilename());
        return "ok";
    }
//    case2-2)여러개의 이미지가 있는 경우
    @PostMapping("/multipart-formdata-images")
    public String multipartFormdataImages(@ModelAttribute Member1 member1, @RequestParam(value = "profileImages") List<MultipartFile> profileImages){
        System.out.println(member1);
        System.out.println(profileImages.size());
        return "ok";
    }

//    case3.body의 content-type이 json
//    case3-1)일반적인 json데이터 처리
//    형식 : {"name":"hongildong", "email":"hong@naver.com"}
    @PostMapping("/json")
//    RequestBody : json데이터를 객체로 파싱
    public String json(@RequestBody Member1 member1){
        System.out.println(member1);
        return "ok";
    }

//    case3-2)배열형식의 json데이터 처리
//    형식 : [{"name":"hongildong", "email":"hong1@naver.com"}, {"name":"hongildong", "email":"hong2@naver.com"}, {"name":"hongildong", "email":"hong3@naver.com"}]
    @PostMapping("/json-list")
//    RequestBody : json데이터를 객체로 파싱
    public String jsonList(@RequestBody List<Member1> member1List){
        System.out.println(member1List);
        return "ok";
    }

//    case 3-3)중첩된 json 데이터 처리
//    데이터형식 : {"name":"hongildong", "email":"hong1@naver.com", "scores":[{"subject":"math", "point":100}, {"subject":"english", "point":90}, {"subject":"korean", "point":100}]}
    @PostMapping("/json-nested")
    public String jsonNested(@RequestBody Member2 member2){
        System.out.println(member2);
        return "ok";
    }
    
//    case3-4)json + file 이 함께 있는 데이터 처리
//    데이터형식 : member={"name":"xx", "email":"yy"}&profileImage=바이너리
//    결론은 multipart-formdata구조안에 json을 넣는 방식.
    @PostMapping("/json-file")
//    json과 file을 함께처리해야할때는 일반적으로 RequestPart 사용
    public String jsonFile(@RequestPart("member") Member1 member1,
                           @RequestPart("profileImage") MultipartFile profileImage
                           ){
        System.out.println(member1);
        System.out.println(profileImage.getOriginalFilename());
        return "ok";
    }
}
