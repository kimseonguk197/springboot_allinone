package com.beyond.basic.p1_http;


import lombok.*;


@Data //getter + setter + toString
@AllArgsConstructor //모든 매개변수를 가진 생성자
//@NoArgsConstructor //매개변수 없는 기본 생성자. 객체바인딩을 위해 반드시 필요
public class Member1 {
//    java에서 class내의 변수는 일반적으로 은닉하기 위해 private으로 생성
    private String name;
    private String email;
//    private MultipartFile profileImage;


////  private변수로 인해 일반적으로 getter setter가 필요
//    public String getName() {
//        return name;
//    }
//    public void setName(String name) {
//        this.name = name;
//    }
//    public String getEmail() {
//        return email;
//    }
//    public void setEmail(String email) {
//        this.email = email;
//    }

////   setter를 사용할 경우 객체변수의 안정성이 떨어지므로 생성자 많이 사용
//    public Member1(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }
//    public Member1() {
//    }

}
