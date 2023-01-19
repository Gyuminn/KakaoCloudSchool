package com.kakao.reviewapp0116.service;

import com.kakao.reviewapp0116.domain.Employee;

public interface EmployeeService {
    Employee createEmployee(String empId, String fname, String sname);
}
