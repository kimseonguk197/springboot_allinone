package com.beyond.basic.p3_board.post.dtos;

import com.beyond.basic.p3_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostDetailDto {
    private Long id;
    private String title;
    private String contents;
    private List<String> authorEmail;
}
