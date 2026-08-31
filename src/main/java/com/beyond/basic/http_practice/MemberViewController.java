package com.beyond.basic.http_practice;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/post-view")
public class MemberViewController {
    @GetMapping("/url-encoded")
    public String urlEncoded(){
        return "1-url_encoded";
    }
    @GetMapping("/multipart-formdata-image")
    public String multiPartFormDataImage(){
        return "2-mutlipart-formdata-image";
    }
    @GetMapping("/multipart-formdata-images")
    public String multiPartFormDataImages(){
        return "2-mutlipart-formdata-images";
    }
    @GetMapping("/json")
    public String jsonView(){
        return "3-json";
    }
    @GetMapping("/json-nested")
    public String jsonViewNested(){
        return "4-json-nested";
    }

    @GetMapping("/json-file")
    public String jsonViewFile(){
        return "5-json-file";
    }
}
