package com.gyumin.springtest0330;

import com.gyumin.springtest0330.dto.HotelRequestDTO;
import com.gyumin.springtest0330.dto.HotelResponseDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import com.gyumin.springtest0330.service.DisplayService;

import java.util.List;

@SpringBootTest
public class ServiceTests {
    @Autowired
    private DisplayService displayService;

    // displayService가 제대로 만들어지는지 확인

    @Autowired
    private ApplicationContext applicatoinContext;

    @Test
    public void testBean() {
        // DisplayService 타입의 bean을 가져오기
        DisplayService displayService1 = applicatoinContext.getBean(DisplayService.class);

        // null 여부 확인
        Assertions.assertNotNull(displayService1);

        // 자료형 확인
        Assertions.assertTrue(DisplayService.class.isInstance(displayService1));
    }

    @Test
    public void testReturn() {
        // Given
        HotelRequestDTO hotelRequestDTO = new HotelRequestDTO("메리어트");

        // When
        List<HotelResponseDTO> hotelResponseDTOList = displayService.getHotelsByName(hotelRequestDTO);

        // Then
        Assertions.assertNotNull(hotelResponseDTOList);
        Assertions.assertNotEquals(0, hotelResponseDTOList.size());
    }
}
