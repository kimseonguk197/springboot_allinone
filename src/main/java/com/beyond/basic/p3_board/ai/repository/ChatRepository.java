package com.beyond.basic.p3_board.ai.repository;

import com.beyond.basic.p3_board.ai.domain.ChatBot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRepository extends JpaRepository<ChatBot, Long> {
}
