package com.beyond.basic.p3_board.ai.tools;


import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class IntentTools {

    @Tool(description = "게시판서비스의 게시글 또는 회원과 관련된 질문일 때 선택")
    public String BOARD() {
        return "BOARD";
    }

    @Tool(description = "게시판 서비스와 관련 없는 일반 질문일 때 선택")
    public String GENERAL() {
        return "GENERAL";
    }
}