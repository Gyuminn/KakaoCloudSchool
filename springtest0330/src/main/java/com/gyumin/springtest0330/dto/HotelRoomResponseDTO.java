package com.gyumin.springtest0330.dto;

import com.gyumin.springtest0330.domain.HotelRoomEntity;
import lombok.Getter;

@Getter
public class HotelRoomResponseDTO {

    private Long hotelRoomId;
    private String code;
    private Integer floor;
    private Integer bedCount;
    private Integer bathCount;

    public HotelRoomResponseDTO(Long hotelRoomId, String code, Integer floor, Integer bedCount, Integer bathCount) {
        this.hotelRoomId = hotelRoomId;
        this.code = code;
        this.floor = floor;
        this.bedCount = bedCount;
        this.bathCount = bathCount;
    }

    // Entity를 DTO로 변환하는 메서드
    public static HotelRoomResponseDTO from(HotelRoomEntity entity) {
        return new HotelRoomResponseDTO(
                entity.getId(), entity.getCode(), entity.getFloor(), entity.getBedCount(), entity.getBathCount()
        );
    }
}
