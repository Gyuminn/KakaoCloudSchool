package com.kakao.reviewapp0116.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Employee {
    private String empId;
    private String firstName;
    private String secondName;
}
