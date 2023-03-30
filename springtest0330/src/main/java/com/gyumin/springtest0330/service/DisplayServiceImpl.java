package com.gyumin.springtest0330.service;

import com.gyumin.springtest0330.dto.HotelRequestDTO;
import com.gyumin.springtest0330.dto.HotelResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Log4j2
@Service
public class DisplayServiceImpl implements DisplayService{
    @Override
    public List<HotelResponseDTO> getHotelsByName(HotelRequestDTO hotelRequestDTO) {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            log.error("error", e.getMessage());
        }
        return List.of(new HotelResponseDTO(1000L, "메리어트", "서울시", "188292"));
    }
}
