package com.gyumin.vlidation0316.controller;

import com.gyumin.vlidation0316.dto.HotelRoomReserveRequestDTO;
import com.gyumin.vlidation0316.dto.ValidRequestDTO;
import com.gyumin.vlidation0316.validator.HotelRoomReserveValidator;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/validation")
@Log4j2
public class ValidationController {
    @PostMapping("/valid")
    public ResponseEntity<String> checkValidationByBalid(
            @Valid @RequestBody ValidRequestDTO validRequestDTO
    ) {
        log.info(validRequestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(validRequestDTO.toString());
    }

    // Validator 인터페이스를 이용해서 유효성 검사를 할 때 사용할 Validator를 등록하는 메서드 - 굉장히 중요
    // 맨 처음 한 번만 수행 - 초기화 메서드
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(new HotelRoomReserveValidator());
    }

    @PostMapping("/reserve")
    public ResponseEntity<?> reserveHotelRoomByRoomNumber(
            @Valid @RequestBody HotelRoomReserveRequestDTO reserveRequestDTO, BindingResult bindingResult
    ) {
        // bindingResult에 유효성 통과 여부를 전달
        if (bindingResult.hasErrors()) {
            FieldError fieldError = bindingResult.getFieldError();
            String errorMessage = new StringBuilder(bindingResult.getFieldError().getCode())
                    .append("[")
                    .append(fieldError.getField())
                    .append("]")
                    .append(fieldError.getDefaultMessage())
                    .toString();
            System.out.println("error: " + errorMessage);
            return ResponseEntity.badRequest().build();
        }
        System.out.println("유효성 검사 통과");
        return ResponseEntity.status(HttpStatus.OK).body("예약 성공");
    }
}
