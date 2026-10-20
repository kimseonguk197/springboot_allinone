package com.beyond.basic.p3_board.author.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AuthorLoginResDto {
    private String accessToken;
    private String refreshToken;
}
