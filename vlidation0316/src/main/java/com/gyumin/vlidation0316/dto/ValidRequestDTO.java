package com.gyumin.vlidation0316.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ValidRequestDTO {
    // 이름은 필수
    @NotBlank
    private String name;

    @Email
    private String email;

    // age의 범위 설정
    @Min(value=20)
    @Max(value=40)
    private int age;

    // 전화번호 정규식
    @Pattern(regexp = "010[.-]?(\\d{3}\\d{4})[.-]?(\\d{4})$")
    private String phoneNumber;

    @Size(min=0, max=40)
    private String description;

    @Positive
    private int count;

    @AssertTrue
    private boolean booleanCheck;


}
