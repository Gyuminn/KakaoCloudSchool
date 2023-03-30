package dto;

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
