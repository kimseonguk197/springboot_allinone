package com.beyond.basic.p3_board.ai.tools;

import com.beyond.basic.p3_board.author.dtos.AuthorDetailDto;
import com.beyond.basic.p3_board.author.dtos.AuthorListDto;
import com.beyond.basic.p3_board.author.service.AuthorService;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

//LLM이 회원(작성자) 관련 조회 API를 호출할 수 있도록 노출하는 tool 모음
@Component
public class AuthorAiTools {

    private final AuthorService authorService;

    @Autowired
    public AuthorAiTools(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Tool(description = "현재 로그인한 사용자 본인의 정보(이름, 이메일 등)를 조회한다.")
    public AuthorDetailDto getMyInfo() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        return authorService.myinfo(email);
    }

    @Tool(description = "전체 회원 목록(id, 이름, 이메일)을 조회한다. 관리자(ADMIN) 권한이 있는 사용자만 사용할 수 있다.")
    public List<AuthorListDto> listAuthors() {
        return authorService.findAll();
    }

}
