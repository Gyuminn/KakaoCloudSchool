package com.kakao.reviewapp0116.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"movie", "member"})
public class Review extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewnum;

    // id를 쓰는 것이 아니라 movie를 쓴다.
    // 리뷰를 가져올 때 movie 정보를 다 가져온다.
    // 따라서 외래키를 넣어주는 것이 아니라 참조하는 데이터 자체를 넣어준다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    private int grade;

    private String text;

    public void changeGrade(int grade) {
        this.grade = grade;
    }

    public void changeText(String tex) {
        this.text = text;
    }
}
