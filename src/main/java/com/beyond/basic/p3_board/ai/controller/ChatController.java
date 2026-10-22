package com.beyond.basic.p3_board.ai.controller;

import com.beyond.basic.p3_board.ai.dtos.ChatRequestDto;
import com.beyond.basic.p3_board.ai.dtos.ChatResponseDto;
import com.beyond.basic.p3_board.ai.service.ChatService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping
    public ResponseEntity<?> chat(@RequestBody @Valid ChatRequestDto dto) {
        String answer = chatService.chat(dto.getMessage());
        return ResponseEntity.status(HttpStatus.OK).body(ChatResponseDto.builder().answer(answer).build());
    }
}
