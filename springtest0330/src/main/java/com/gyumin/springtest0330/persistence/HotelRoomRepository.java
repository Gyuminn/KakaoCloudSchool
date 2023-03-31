package com.gyumin.springtest0330.persistence;

import com.gyumin.springtest0330.domain.HotelRoomEntity;
import org.springframework.stereotype.Repository;

@Repository
public class HotelRoomRepository {
    // 더미데이터를 만들면 테스트가 가능
    public HotelRoomEntity findById(Long id) {
        return new HotelRoomEntity(id, "EAST_1999", 20, 2, 1);
    }
}
