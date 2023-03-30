package com.gyumin.springtest0330.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HotelRequestDTO {
    private String hotelName;

    public HotelRequestDTO() {

    }

    public HotelRequestDTO(String hotelName) {
        this.hotelName = hotelName;
    }
}
