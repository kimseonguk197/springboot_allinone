package com.beyond.basic.p2_db.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberReqDto {
    @NotBlank(message = "이름은 비어있으면 안됩니다.")
    @Size(min = 8, message = "이름의 길이가 너무 짧습니다.")
    private String name;
    private String email;
}
