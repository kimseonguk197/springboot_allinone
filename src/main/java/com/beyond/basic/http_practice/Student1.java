package com.beyond.basic.http_practice;


import lombok.*;

//@Data : @Getter + @Setter + @ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student1 {
    private String name;
    private String email;
////    MultipartFile : java/spring에서 파일처리 편의를 제공해주는 클래스
//    private MultipartFile profileImage;
}
