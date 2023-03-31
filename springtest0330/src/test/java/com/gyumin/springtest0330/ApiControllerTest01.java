package com.gyumin.springtest0330;

import com.gyumin.springtest0330.controller.HotelController;
import com.gyumin.springtest0330.dto.HotelRequestDTO;
import com.gyumin.springtest0330.dto.HotelResponseDTO;
import com.gyumin.springtest0330.service.DisplayService;
import com.gyumin.springtest0330.util.JsonUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

// 가짜 웹 환경을 만듬.
@WebMvcTest(controllers = HotelController.class)
@AutoConfigureMockMvc
public class ApiControllerTest01 {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DisplayService displayService;

    // 테스트 메서드가 수행되기 전에 호출
    @BeforeEach
    public void init() {
        given(displayService.getHotelsByName(any()))
                .willAnswer(new Answer<List<HotelResponseDTO>>() {
                    @Override
                    public List<HotelResponseDTO> answer(InvocationOnMock invocation) throws Throwable {
                        HotelRequestDTO hotelRequestDTO = invocation.getArgument(0);
                        return List.of(new HotelResponseDTO(1L, hotelRequestDTO.getHotelName(), "unknown", "213"));
                    }
                });
    }

    @Test
    public void testGetHotelById() throws Exception {
        // RequestBody 생성
        HotelRequestDTO hotelRequestDTO = new HotelRequestDTO("Ragged Point");

        // 객체의 내용을 Json 문자열로 변경
        String jsonRequest = JsonUtil.objectMapper.writeValueAsString(hotelRequestDTO);

        // 요청 처리 생성
        mockMvc.perform(post("/hotels/fetch-by-name")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].hotelId", Matchers.is(1000)))
                .andExpect(jsonPath("$[0].hotelName", Matchers.is("Ragged Point")))
                .andDo(MockMvcResultHandlers.print(System.out));
    }
}