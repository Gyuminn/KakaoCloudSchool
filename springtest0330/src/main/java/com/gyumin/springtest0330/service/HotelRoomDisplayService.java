package com.gyumin.springtest0330.service;

import com.gyumin.springtest0330.domain.HotelRoomEntity;
import com.gyumin.springtest0330.dto.HotelRoomResponseDTO;
import com.gyumin.springtest0330.persistence.HotelRoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class HotelRoomDisplayService {
    private final HotelRoomRepository hotelRoomRepository;
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public HotelRoomDisplayService(HotelRoomRepository hotelRoomRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.hotelRoomRepository = hotelRoomRepository;
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    // 테스트할 메서드
    public HotelRoomResponseDTO getHotelRoomById(Long id) {
        HotelRoomEntity hotelRoomEntity = hotelRoomRepository.findById(id);
        // 스레드 풀을 이용해서 스레드 생성 및 수행
        threadPoolTaskExecutor.execute(() -> log.warn("entity:{}", hotelRoomEntity.toString()));

        return HotelRoomResponseDTO.from(hotelRoomEntity);
    }
}
