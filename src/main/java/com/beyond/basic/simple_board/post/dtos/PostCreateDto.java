package com.beyond.basic.simple_board.post.dtos;

import com.beyond.basic.simple_board.author.domain.Author;
import com.beyond.basic.simple_board.post.domain.Post;
import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostCreateDto {
    private String title;
    private String category;
    private String contents;
//    private String authorEmail;
    @Builder.Default
    private String appointment = "N";
    @Builder.Default
    private LocalDateTime appointmentTime=LocalDateTime.now();

    public Post toEntity(Author author){
        return Post.builder()
                .title(this.title)
                .category(this.category)
                .contents(this.contents)
                .author(author)
//                .delYn("N")
                .appointment(this.appointment)
                .appointmentTime(this.appointmentTime)
                .build();
    }
}
