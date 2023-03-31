package com.gyumin.springtest0330;

import com.gyumin.springtest0330.domain.HotelRoomEntity;
import com.gyumin.springtest0330.dto.HotelRoomResponseDTO;
import com.gyumin.springtest0330.persistence.HotelRoomRepository;
import com.gyumin.springtest0330.service.HotelRoomDisplayService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

// static import
// 클래스의 특정 static 멤버만 import
// 가독성을 높이기 위해서 수행
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@SpringBootTest
public class HotelRoomDisplayServiceTest2 {
    @Autowired
    private HotelRoomDisplayService hotelRoomDisplayService;

    // 가짜로 객체 만들기
    @MockBean
    private HotelRoomRepository hotelRoomRepository;

    @Test
    public void testMockBean() {
        // getHotelRoomById가 지금 구현되있긴 한데,
        // 실제로 구현이 안된 메서드를 호출해서 결과를 만들어 낼 수 있다.
        // HotelRoomRepository를 Interface로 바꾸고 메서드를 public HotelRoomEntity findById(Long id) 라고만 바꿔보자.
        // 그리고 아래를 수행하면 수행이 잘 된다.
        // any()는 아무것이나 대입하면 결과를 만들어주겠다는 의미이다.
        // 이렇게 가짜로 메서드를 수행하는 경우 이 메서드를 stub이라고 한다.
        given(this.hotelRoomRepository.findById(any()))
                .willReturn(new HotelRoomEntity(10L, "test", 1, 1, 1));

        HotelRoomResponseDTO hotelRoomResponseDTO = hotelRoomDisplayService.getHotelRoomById(1L);
        Assertions.assertNotNull(hotelRoomResponseDTO);
        Assertions.assertEquals(10L, hotelRoomResponseDTO.getHotelRoomId());
    }
}
