package com.gyumin.vlidation0316.validator;

import com.gyumin.vlidation0316.dto.HotelRoomReserveRequestDTO;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

public class HotelRoomReserveValidator implements Validator {
    // 유효성 검사를 수행할 클래스 가능 여부를 리턴
    @Override
    public boolean supports(Class <?> clazz) {
        // DTO를 검사하겠다.
        return HotelRoomReserveRequestDTO.class.equals(clazz);
    }

    // 유효성 검사를 수행하는 메서드
    @Override
    public void validate(Object target, Errors errors) {
        // target이 실제 유효성 검사를 수행할 객체이므로 casting
        // HotelRoomReserveRequestDTO request = (HotelRoomReserveRequestDTO) target; - 에전에 형번환하던 방법
        HotelRoomReserveRequestDTO request = HotelRoomReserveRequestDTO.class.cast(target);

        // checkInDate가 Null인 경우
        if (Objects.isNull(request.getCheckInDate())) {
            errors.rejectValue("checkInDate", "NotNull", "checkInDate is null");
        }

        // checkInDate가 Null인 경우
        if (Objects.isNull(request.getCheckOutDate())) {
            errors.rejectValue("checkOutDate", "NotNull", "checkOutDate is null");
        }

        if (request.getCheckInDate().compareTo(request.getCheckOutDate()) >= 0) {
            errors.rejectValue("checkOutDate", "Constraint Error",
                    "체크아웃 날짜가 체크인 날짜보다 늦어야 합니다.");
        }
    }
}
