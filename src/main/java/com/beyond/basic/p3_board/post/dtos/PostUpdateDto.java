package com.beyond.basic.p3_board.post.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostUpdateDto {
    private Long postId;
    private String contents;
    private String authorEmail;

}
