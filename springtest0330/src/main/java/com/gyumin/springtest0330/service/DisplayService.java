package com.gyumin.springtest0330.service;

import com.gyumin.springtest0330.dto.HotelRequestDTO;
import com.gyumin.springtest0330.dto.HotelResponseDTO;

import java.util.List;

public interface DisplayService {
    List<HotelResponseDTO> getHotelsByName(HotelRequestDTO hotelRequestDTO);
}
