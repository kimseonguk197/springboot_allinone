package com.beyond.basic.p2_db.domain;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
//@Data
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(nullable = false, length = 20, unique = true)
    private String email;
//    @Builder.Default
//    private LocalDateTime created_Time= LocalDateTime.now();
    @CreationTimestamp
    private LocalDateTime created_Time;

//    public Member(String name, String email) {
//        this.name = name;
//        this.email = email;
//    }
}
