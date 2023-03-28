package com.example.batch_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TranscationDTO {
    private String accountNumber;
    // 외부 시스템에서 만들어진 텍스트를 날짜 형식으로 바로 변환해서 사용하고자 하는 경우
    // SimpleDateFormat을 알아야 한다.
    // Mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));
    private Date transactionDate;
    private Double amount;
}
