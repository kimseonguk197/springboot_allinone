package com.beyond.basic.simple_board.post.domain;

import com.beyond.basic.simple_board.author.domain.Author;
import com.beyond.basic.simple_board.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter @ToString
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;
    private String category;
    @Column(length = 3000)
    private String contents;
//    @Column(nullable = false)
//    private Long authorId;

//    ManyToOne을 통해 fk설정(author_id컬럼)
//    ManyToOne을 통해 author_id컬럼으로 author객체 조회 및 객체자동생성
//    fetch lazy(지연로딩) : author객체를 사용하지 않는한, author객체 생성X(서버부하감소)
    @ManyToOne(fetch = FetchType.LAZY)
//    ManyToOne 어노테이션만 추가하더라도, 아래 옵션이 생략돼 있는것. fk를 설정하지 않고자 할때, NO_CONSTRAINT 설정
    @JoinColumn(name = "author_id", foreignKey = @ForeignKey(ConstraintMode.CONSTRAINT), nullable = false)
    private Author author;
    @Builder.Default
    private String delYn="N";

    @Builder.Default
    private String appointment="N";
    @Builder.Default
    private LocalDateTime appointmentTime = LocalDateTime.now();

    public void deletePost(){
        this.delYn = "Y";
    }

    public void updateAppointment(String appointment){
        this.appointment = appointment;
    }
}
