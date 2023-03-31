package com.gyumin.springtest0330;

import com.gyumin.springtest0330.dto.HotelRoomResponseDTO;
import com.gyumin.springtest0330.service.HotelRoomDisplayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
// 설정 클래스를 지정
@ContextConfiguration(classes = TestThreadPoolConfig.class)
// 설정 파일을 지정
@TestPropertySource(locations = "classpath:application-test.properties")
public class HotelRoomDisplayServiceTests {
    @Autowired
    private HotelRoomDisplayService hotelRoomDisplayService;

    @Test
    public void testConfiguration() {
        HotelRoomResponseDTO hotelRoomResponseDTO = hotelRoomDisplayService.getHotelRoomById(1L);

        Assertions.assertNotNull(hotelRoomResponseDTO);
        Assertions.assertEquals(1L, hotelRoomResponseDTO.getHotelRoomId());
    }
}
