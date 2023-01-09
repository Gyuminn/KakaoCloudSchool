package com.kakao.springboot0109.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ParamDTO {
    private String name;
    private String email;
    private String organization;
}
