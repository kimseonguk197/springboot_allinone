package com.beyond.basic.p3_board.post.dtos;

import com.beyond.basic.p3_board.author.domain.Author;
import com.beyond.basic.p3_board.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostCreateDto {
    private String title;
    private String contents;
//    private String authorEmail;
    @Builder.Default
    private String appointment = "N";
    @Builder.Default
    private LocalDateTime appointmentTime=LocalDateTime.now();

}
