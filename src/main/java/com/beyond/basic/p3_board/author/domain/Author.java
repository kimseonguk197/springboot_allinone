package com.beyond.basic.p3_board.author.domain;

import com.beyond.basic.p3_board.common.domain.BaseTimeEntity;
import com.beyond.basic.p3_board.post.domain.AuthorPost;
import com.beyond.basic.p3_board.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Author extends BaseTimeEntity {
    @Id //pk설정
//    identity : auto_increment설정. auto:id생성전략을 jpa에게 자동설정하도록 위임.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    Long -> bigint, String -> varchar
    private Long id;
//    변수명이 컬럼명으로 그대로 생성. camelcase는 언더스코어로 변경. 예)nickName -> nick_name
    private String name;
//    길이(varchar50, 디폴트-varchar255), 제약조건(unique, not null) 설정
    @Column(length = 50, unique = true, nullable = false)
    private String email;
//    @Column(name = "pw") // 컬럼명의 변경이 가능하나, 일반적으로 일치시킴.
//    @Setter
    private String password;

//    enum타입은 내부적으로 숫자값을 가지고 있으나, 문자형태로 저장하겠다는 어노테이션
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    public Author(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Address address;


//    @OneToOne(mappedBy = "author", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    private Address address;

    private String profileImageUrl;

    public void updateProfileImageUrl(String profileImageUrl){
        this.profileImageUrl = profileImageUrl;
    }
    public void updatePassword(String password){
        this.password = password;
    }
}
