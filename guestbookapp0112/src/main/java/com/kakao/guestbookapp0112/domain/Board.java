package com.kakao.guestbookapp0112.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer") // toString을 만들 때 writer의 toString은 호출 안함.
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    // 처음에는 가져오지 않고 사용을 할 때 가져온다.
    private Member writer;
}