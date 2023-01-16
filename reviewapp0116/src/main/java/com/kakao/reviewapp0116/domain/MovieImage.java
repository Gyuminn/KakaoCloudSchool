package com.kakao.reviewapp0116.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
// toString을 할 때 movie는 제외
// 지연 생성이기 때문에 get을 하지 않은 상태에서 toString을 호출하면
// NullPointerException이 발생
@ToString(exclude = "movie")
// 부모 테이블을 만들 때 이 속성의 값을 포함시켜 생성해주세요 라는 뜻.
// 컬럼의 어노테이션에 @Embedded를 추가하면 부모 테이블이 생성될때 들어감.
// 1대 다 관계에서는 많이 사용하지 않고 1대 1관게에서 테이블을 나누어 설계할 때 주로 사용된다.
@Embeddable
public class MovieImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inum;

    private String uuid; // 파일의 이름이 겹치지 않도록 하기 위해서 추가

    private String imgName; // 파일 이름

    // 하나의 디렉토리에 너무 많은 파일이 저장되지 않도록 업로드한 날짜 별로 파일을 기록하기 위한 디렉토리 이름
    // (aws의 경우는 안만들어도 됨.)
    private String path;

    // 하나의 Movie가 여러 개의 MovieImage를 소유
    // 데이터를 불러올 때 movie를 불러오지는 않고 사용할 때 불러옴.
    // 외래키의 이름은 안쓰면 movie_mno로 만들어진다.
    @ManyToOne(fetch = FetchType.LAZY)
    private Movie movie;
}
