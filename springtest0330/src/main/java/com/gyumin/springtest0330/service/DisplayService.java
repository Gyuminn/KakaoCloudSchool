package service;

import dto.HotelRequestDTO;
import dto.HotelResponseDTO;

import java.util.List;

public interface DisplayService {
    List<HotelResponseDTO> getHotelsByName(HotelRequestDTO hotelRequestDTO);
}
