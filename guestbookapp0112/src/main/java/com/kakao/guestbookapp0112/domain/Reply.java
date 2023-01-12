package com.kakao.guestbookapp0112.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Reply extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long rno;

    private String text;

    private String replyer;

    @ManyToOne
    private Board board;
}
