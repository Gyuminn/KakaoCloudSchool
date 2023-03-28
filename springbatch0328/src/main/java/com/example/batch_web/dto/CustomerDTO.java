package com.example.batch_web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String addressNumber;
    private String street;
    private String city;
    private String state;
    private String zipCode;

    // DTO안에 배열이나 List가 존재하는 경우
    // 배열이나 List의 데이터 1개를 수정하거나 가져오는 메서드를 같이 생성
    // Map이 존재하는 경우 Map에 저장하거나 하나의 key의 값을 가져오는 것을 같이 생성한다.
    private List<TranscationDTO> transactions;

    public void setTransaction(int idx, TranscationDTO transcationDTO) {
        if (idx >= 0 && idx < transactions.size()) {
            transactions.set(idx, transcationDTO);
        }
    }

    public TranscationDTO getTransaction(int idx) {
        if (idx < 0 || idx >= transactions.size()) {
            return null;
        }

        return transactions.get(idx);
    }
}
