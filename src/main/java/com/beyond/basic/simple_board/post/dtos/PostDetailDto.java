package com.beyond.basic.simple_board.post.dtos;

import com.beyond.basic.simple_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostDetailDto {
    private Long id;
    private String title;
    private String category;
    private String contents;
    private String authorEmail;
//    public static PostDetailDto fromEntity(Post post, Author author){
    public static PostDetailDto fromEntity(Post post){
        return PostDetailDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .category(post.getCategory())
                .contents(post.getContents())
//                .authorEmail(author.getEmail())
                .authorEmail(post.getAuthor().getEmail())
                .build();
    }
}
