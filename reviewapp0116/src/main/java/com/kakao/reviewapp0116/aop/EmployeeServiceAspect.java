package com.kakao.reviewapp0116.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component // bean을 자동으로 생성해주는 어노테이션 - Controller, Service, Repository, RestController, Configuration 도 마찬가지
public class EmployeeServiceAspect {
    // EmployeeService 메서드가 호출되기 전에 수행
    @Before(value="execution(* com.kakao.reviewapp0116.service.EmployeeService.*(..)) and args(empId, fname, sname)")
    public void beforeAdvice(JoinPoint joinPoint, String empId, String fname, String sname) {
        System.out.println("메서드 호출하기 전에 호출");
    }

    @After(value="execution(* com.kakao.reviewapp0116.service.EmployeeService.*(..)) and args(empId, fname, sname)")
    public void afterAdvice(JoinPoint joinPoint, String empId, String fname, String sname) {
        System.out.println("메서드 호출해서 수행한 후 호출");
    }
}
