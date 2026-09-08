package com.beyond.basic.p3_board.author.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private String street;
    private String zipCode;

    @OneToOne(fetch = FetchType.LAZY)
//    @OneToOne
//    @JoinColumn(name = "author_id", unique = true ,foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT), nullable = false)
    private Author author;

}
