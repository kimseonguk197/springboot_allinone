package com.beyond.basic.http_practice;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

//Controller 어노테이션을 통해 스프링에 의해 객체가 생성되고, 관리되어 개발자가 직접 객체를 생성할필요 없음.
//Controller 어노테이션과 url매핑을 통해 사용자의 요청이 메서드로 분기처리
@Controller
@RequestMapping("/member")
public class MemberMvcController {
//    get요청 리턴의 case : text, json, html(mvc)
//    case1. 서버가 사용자에게 text데이터 return
    @GetMapping("")
//    data(text, json)를 리턴할때에는 responsebody 사용. 화면(html)을 리턴할때에는 responsebody생략.
//    controller+responsebody=restcontroller
    @ResponseBody
    public static String textDataReturn(){
        return "hongildong1";
    }

//    case2.서버가 사용자에게 json형식의 문자데이터 return
    @GetMapping("/json")
    @ResponseBody
    public Member1 jsonDataReturn() throws JsonProcessingException {
        Member1 m1 = new Member1("h1", "h1@naver.com");
//        직접 json을 직렬화 할필요 없이, return 타입에 객체가 있다면 자동으로 직렬화
//        ObjectMapper o1 = new ObjectMapper();
//        String data = o1.writeValueAsString(m1);
        return m1;
    }

//    case3. 서버가 사용자에게 html return
//    case3-1)정적인 html return
    @GetMapping("/html")
//    ResponseBody가 없고, return타입이 String인 경우 스프링은 templates폴더 밑에 simple_html.html을 찾아서 리턴.
//    타임리프 의존성이 필요.
    public String htmlReturn(){
       return "simple_html";
    }

//    case3. 서버가 사용자에게 html return
//    case3-2)서버에서 화면+데이터를 함께 주는 동적인 화면
//    현재 이 방식은 ssr(서버사이드렌더링)방식. csr방식은 화면은 별도제공하고, 서버는 데이터만 제공.
    @GetMapping("/html/dynamic")
//    ResponseBody가 없고, return타입이 String인 경우 스프링은 templates폴더 밑에 simple_html.html을 찾아서 리턴.
//    타임리프 의존성이 필요.
    public String dynamicHtmlReturn(Model model){
//        model객체는 데이터를 화면에 전달해주는 역할
//        name=hongildong 형태로 화면에 전달
        model.addAttribute("name", "hongildong");
        model.addAttribute("email", "hongildong@naver.com");
        return "dynamic_html";
    }

}
