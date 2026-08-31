package com.beyond.basic.simple_board.author.dtos;

import com.beyond.basic.simple_board.author.domain.Author;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AuthorListDto {
    private Long id;
    private String name;
    private String email;
    public static AuthorListDto fromEntity(Author author){
        return AuthorListDto.builder()
                .id(author.getId())
                .name(author.getName())
                .email(author.getEmail())
                .build();
    }
}
