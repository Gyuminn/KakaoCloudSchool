package com.gyumin.springtest0330.domain;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
// @Entity - 처음에는 안붙인다 - 왜냐면 붙이면 바로 DB 연동이 되고 쓸데없는 데이터가 들어갈 수 있다.
// 그러니까 테스트가 끝나고 붙여도 된다.
public class HotelRoomEntity {
    private Long id;
    private String code;
    private Integer floor;
    private Integer bedCount;
    private Integer bathCount;

    public HotelRoomEntity(Long id, String code, Integer floor, Integer bedCount, Integer bathCount) {
        this.id = id;
        this.code = code;
        this.floor = floor;
        this.bedCount = bedCount;
        this.bathCount = bathCount;
    }
}
