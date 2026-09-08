package com.beyond.basic.p3_board.post.domain;

import com.beyond.basic.p3_board.author.domain.Author;
import com.beyond.basic.p3_board.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
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

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
    @Builder.Default
    private List<AuthorPost> authorPostList = new ArrayList<>();

    public void updateContents(String contents){
        this.contents = contents;
    }

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
