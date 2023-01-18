package com.kakao.reviewapp0116.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MovieDTO {
    private Long mno;
    private String title;

    // review의 grade 평균
    private double avg;

    // 리뷰 개수
    private Long reviewCnt;

    // 등록일과 수정
    private LocalDateTime regDate;
    private LocalDateTime modDate;

    // 영화 이미지도 같이 등록
    // Scala 데이터는 초기화될 때 없으면 null인데 null이어도 된다.
    // 그런데 Vector 데이터는 초기화될 때 데이터가 없더라도 인스턴스 생성을 해줘야 한다. 안 그러면 null pointer exception 에러가 난다.
    // 따라서 아래와 같이 초기화해준다.
    // builder()라는 메서드를 이용해서 생성할 때 기본으로 사용하겠다는 뜻.
    @Builder.Default
    private List<MovieImageDTO> imageDTOList = new ArrayList<>();
}
