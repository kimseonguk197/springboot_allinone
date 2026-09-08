package com.beyond.basic.p1_http;

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
}
