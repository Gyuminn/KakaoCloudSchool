package dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HotelResponseDTO {
    private Long hotelId;
    private String hotelName;
    private String address;
    private String phoneNumber;

    public HotelResponseDTO(Long hotelId, String hotelName, String address, String phoneNumber) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}
