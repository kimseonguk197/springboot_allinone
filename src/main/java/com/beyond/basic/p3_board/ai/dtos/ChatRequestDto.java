package com.beyond.basic.p3_board.ai.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ChatRequestDto {
    @NotBlank(message = "message가 비어있으면 안됩니다.")
    private String message;
}
