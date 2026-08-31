package com.beyond.basic.http_practice;


import lombok.*;

//@Data : @Getter + @Setter + @ToString
@Data
@AllArgsConstructor //모든 매개변수 생성자
@NoArgsConstructor //기본생성자
public class Member1 {
    private String name;
    private String email;
////    MultipartFile : java/spring에서 파일처리 편의를 제공해주는 클래스
//    private MultipartFile profileImage;
}
