package com.gyumin.springtest0330.controller;

import com.gyumin.springtest0330.domain.HotelRoomEntity;
import com.gyumin.springtest0330.dto.HotelRequestDTO;
import com.gyumin.springtest0330.dto.HotelResponseDTO;
import com.gyumin.springtest0330.dto.HotelRoomResponseDTO;
import com.gyumin.springtest0330.service.DisplayService;
import com.gyumin.springtest0330.service.HotelRoomDisplayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
public class HotelController {
    private final DisplayService displayService;

    @ResponseBody
    @PostMapping("/hotels/fetch-by-name")
    public ResponseEntity<List<HotelResponseDTO>> getHotelByName(@RequestBody HotelRequestDTO hotelRequestDTO) {
        List<HotelResponseDTO> hotelResponses = displayService.getHotelsByName(hotelRequestDTO);
        return ResponseEntity.ok(hotelResponses);
    }

}
