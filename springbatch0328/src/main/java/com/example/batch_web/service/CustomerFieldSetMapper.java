package com.example.batch_web.service;

import com.example.batch_web.dto.CustomerDTO;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;


public class CustomerFieldSetMapper implements FieldSetMapper<CustomerDTO> {
    // csv 파일을 읽어서 가져온 Field와 DTO의 필드를 매핑하는 메서드
    @Override
    public CustomerDTO mapFieldSet(FieldSet fieldSet) throws BindException {
        CustomerDTO customerDTO = new CustomerDTO();

        customerDTO.setFirstName(fieldSet.readString("firstName"));
        customerDTO.setLastName(fieldSet.readString("lastName"));
        customerDTO.setMiddleInitial(fieldSet.readString("middleInitial"));
        customerDTO.setCity(fieldSet.readString("city"));
        customerDTO.setStreet(fieldSet.readString("street"));
        customerDTO.setAddressNumber(fieldSet.readString("addressNumber"));
        customerDTO.setState(fieldSet.readString("state"));
        customerDTO.setZipCode(fieldSet.readString("zipCode"));

        return customerDTO;
    }
}
