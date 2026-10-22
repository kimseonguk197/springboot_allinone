package com.beyond.basic.p3_board.ai.service;

import com.beyond.basic.p3_board.ai.domain.ChatBot;
import com.beyond.basic.p3_board.ai.repository.ChatRepository;
import com.beyond.basic.p3_board.ai.tools.AuthorAiTools;
import com.beyond.basic.p3_board.ai.tools.IntentTools;
import com.beyond.basic.p3_board.ai.tools.PostAiTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ChatService {

    private final ChatClient chatClient;
    private final ChatRepository chatRepository;
    private final PostAiTools postAiTools;
    private final AuthorAiTools authorAiTools;
    private final IntentTools intentTools;

    @Autowired
    public ChatService(ChatClient.Builder chatClientBuilder, PostAiTools postAiTools, AuthorAiTools authorAiTools, ChatRepository chatRepository, IntentTools intentTools) {
//        tool은 라우팅 결과에 따라 요청별로 선택적으로 붙이므로, 빌더에는 기본 tool을 등록하지 않는다.
        this.chatClient = chatClientBuilder.build();
        this.postAiTools = postAiTools;
        this.authorAiTools = authorAiTools;
        this.chatRepository = chatRepository;
        this.intentTools = intentTools;
    }

    public String chat(String message) {
        boolean boardRelated = isBoardRelated(message);
        String answer = "";
        ChatClient.ChatClientRequestSpec requestSpec = chatClient.prompt().user(message);
        if (boardRelated) {
//            회원/게시글 관련 질문 : tool calling으로 기존 API를 호출
            requestSpec = requestSpec.tools(postAiTools, authorAiTools);
            answer = requestSpec.call().content();
        }else{
//            일반 질문 : LLM을 통해 일반 응답을 생성
            answer = requestSpec.call().content();
        }


        chatRepository.save(ChatBot.builder()
                .request(message)
                .response(answer)
                .build());

        return answer;
    }

//    별도의 tool 없이 LLM 자체 판단으로 질문을 BOARD/GENERAL 두 갈래로 분류
    private boolean isBoardRelated(String message) {
        String SYSTEM_PROMPT = """
            USER의 질문이 이 게시판 서비스의 회원 또는 게시글과 관련이 있으면 "BOARD" 라고만 답하라.
            게시판 서비스와 무관한 질문이면 "GENERAL" 이라고만 답해.
            다른 설명 없이 BOARD 또는 GENERAL 중 한 단어만 출력해.
            """;
        String intent = chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(message)
//                .tools(intentTools)
                .call()
                .content();
        System.out.println(intent);
        return intent != null && intent.trim().toUpperCase().startsWith("BOARD");
    }
}
