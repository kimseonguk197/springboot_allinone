package com.beyond.basic.http_practice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Member2 {
    private String name;
    private String email;
    private List<Score> scores;
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class Score{
        private String subject;
        private int point;
    }
}
