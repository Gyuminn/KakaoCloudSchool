package com.kakao.reviewapp0116.controller;

import com.kakao.reviewapp0116.domain.Employee;
import com.kakao.reviewapp0116.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;

@RestController
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping("/add/employee")
    public ResponseEntity<Employee> addEmployee(
            @RequestParam("empId") String empId,
            @RequestParam("firstName") String firstName,
            @RequestParam("secondName") String secondName) {
        Employee employee = employeeService.createEmployee(empId, firstName, secondName);
        // 요즘은 headers 설정을 안해줘도 된다고 함.
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        return new ResponseEntity<>(employee, headers, HttpStatus.OK);
    }

}
