package com.beyond.basic.p3_board.post.dtos;

import com.beyond.basic.p3_board.post.domain.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class PostListDto {
    private Long id;
    private String title;
}
