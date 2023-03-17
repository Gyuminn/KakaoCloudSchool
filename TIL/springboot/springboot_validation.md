# [Spring Boot] 유효성 검사

1. **유효성 검사**

   입력 데이터의 적합성을 검사하는 것

   유효성 검사를 적절히 수행하면 Server Application에서 NullPointerException이나 예상하지 못한 오류를 사전에 회피할 수 있음.

   1. 위치에 따른 분류
      - Clinet에서 Server로 전송하기 전
        입력 도중(Changed 이벤트)에 하기도 하고 전송하기 직전(Submit 이벤트)에 하기도 함
        서버로 전송하지 않기 때문에 트래픽을 줄일 수 있고 결과를 바로 확인 가능한데 보안이 취약하다는 단점이 있다.
      - Server가 클라이언트로 요청을 받은 후
        Controller에서 할 수도 있고 Service나 Repository에서 할 수도 있지만 특별한 경우가 아니면 입력 데이터는 Controller에서 하고 조회하는 데이터는 Service에서 수행
        Service에서 중복 여부, 존재 여부 등을 확인하는 것은 필수
        보안이 우수하지만 결과를 다시 전송해야 하기 때문에 트래픽이 증가
        Client에서 유효성 검사를 하더라도 Server에서 다시 수행하는 것이 좋다.
      - Data Store에 적용하기 직전
        제약 조건으로 설정
   2. Spring(Server)에서 유효성 검사 방법
      - JSR-303 Spec에서 제공하는 어노테이션을 이용하는 방법 - 구현체는 Hibernate Validation - 이 방법을 요즘에는 많이 이용함.
      - Validator 구현 클래스와 InitBinder를 이용하여 검증

2. **유효성 검사를 위한 프로젝트 생성**

   1. 의존성

      Spring Boot Device, Lombok, Spring Web, Spring Data JPA, 데이터베이스 드라이버,

   2. application.yml 작성

      ```yaml
      server:
        port: 80

      spring:
        datasource:
          url: jdbc:mariadb://localhost:3306/gyumin
          driver-class-name: org.mariadb.jdbc.Driver
          username: root
          password: root

        jpa:
          hibernate:
            ddl-auto: update
          properties:
            hibernate:
              format_sql: true
              show_sql: true
      logging:
        level:
          org:
            hibernate:
              type:
                descriptor:
                  sql: trace
            springframework:
              security: trace

      com:
        kakao:
          apiserver:
            secret: adam1234567890
      ```

3. **Spring Boot 유효성 검사 - JSR-303**

   유효성 검사는 각 계층에서 데이터가 넘어오는 시점에 수행하기 때문에 DTO 클래스에서 수행

   1. 유효성 검사 Annotation
      - 문자열 검증
        - @Null: null만 허용
        - @NotNull: null을 허용하지 않고 “”, “ “는 허용
        - @NotEmpty: null과 “”는 허용하지 않고 “ “는 허용
        - @NotBlack: null, “”, “ “모두 허용하지 않음
      - 최댓값이나 최솟값 검증: BicDecimal, BigInteger 등의 숫자 자료형에만 적용
        - @DecimalMax(value=”$숫자”): 숫자보다 작은 값만 허용
        - @DecimalMin(value=”$숫자”): 숫자보다 큰 값만 허용
        - @Miin(value=$숫자): 숫자보다 큰 값만 허용
        - @Max(value=$숫자): 숫자모다 작은 값만 허용
      - 값의 범위 검증: BigDecimal, BigInteger 등의 숫자 자료형에만 적용
        - @Positive
        - @PositiveOrZero
        - @Negative
        - @NegativeOrZero
      - 시간에 대한 검증: Date, LocalDate, LocalDateTime 등의 타입을 지원
        - @Future
        - @FutureOrPresent
        - @Past
        - @PastOrPresent
      - 이메일 검증
        - @Email
      - 숫자 자리수 검증
        - @Digits(integer=$숫자1, fraction=$숫자2)
          정수는 숫자 1만큼이고 소수는 숫자2 만큼의 자릿수를 허용
      - Boolean 검증
        - @AssertTrue
        - @AssertFalse
      - 문자열 길이 검증
        - @Size(min=$숫자1, max=$숫자2)
      - 정규식 검증
        - @Pattern(regexp=”$정규식”)
   2. DTO 클래스 생성 - dto.ValidReqeustDTO

      ```java
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
      ```

   3. 확인을 위한 컨트롤러 생성 - controller.ValidationController

      ```java
      package com.gyumin.vlidation0316.controller;

      import com.gyumin.vlidation0316.dto.ValidRequestDTO;
      import jakarta.validation.Valid;
      import lombok.extern.log4j.Log4j2;
      import org.springframework.http.HttpStatus;
      import org.springframework.http.ResponseEntity;
      import org.springframework.web.bind.annotation.PostMapping;
      import org.springframework.web.bind.annotation.RequestBody;
      import org.springframework.web.bind.annotation.RequestMapping;
      import org.springframework.web.bind.annotation.RestController;

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
      }
      ```

   4. 테스트용 클라이언트를 만들거나 POSTMAN 같은 프로그램을 이용해서 테스트

      유효성 검사를 통과할 데이터를 전송하면 응답이 나오지만 그렇지 않은 경우 400 에러를 발생시키고 trace 부분을 확인하면 어떤 데이터가 유효성 검사를 통과하지 못했는지 확인이 가능

4. **Spring Boot에서의 유효성 검사 - Validator 인터페이스 이용**

   1. Validator 인터페이스 메서드
      - supports
        제공하는 클래스가 검증할 수 있는 클래스인지 확인하는 메서드
      - validate(Object target, Errors errors)
        target이 검증하고자 하는 객체라서 캐스팅해서 사용하는데 유효성을 직접 만들어서 유효성 검사에 실패했을 때 내용을 errors에 저장할 수 있다.
   2. 유효성 검사를 위한 DTO 클래스를 생성 - dto.HotelRoomReserveRequestDTO

      ```java
      @Getter
      @ToString
      public class HotelRoomReserveRequestDTO {
          private LocalDate checkInDate;

          private LocalDate checkOutDate;

          private String name;
      }
      ```

   3. 유효성 검사를 위한 Validator 클래스 생성 - validator.HotelRoomReserveValidator

      ```java
      package com.gyumin.vlidation0316.validator;

      import com.gyumin.vlidation0316.dto.HotelRoomReserveRequestDTO;
      import org.springframework.validation.Errors;
      import org.springframework.validation.Validator;

      import java.util.Objects;

      public class HotelRoomReserveValidator implements Validator {
          // 유효성 검사를 수행할 클래스 가능 여부를 리턴
          @Override
          public boolean supports(Class <?> clazz) {
              // DTO를 검사하겠다.
              return HotelRoomReserveRequestDTO.class.equals(clazz);
          }

          // 유효성 검사를 수행하는 메서드
          @Override
          public void validate(Object target, Errors errors) {
              // target이 실제 유효성 검사를 수행할 객체이므로 casting
              // HotelRoomReserveRequestDTO request = (HotelRoomReserveRequestDTO) target; - 에전에 형번환하던 방법
              HotelRoomReserveRequestDTO request = HotelRoomReserveRequestDTO.class.cast(target);

              // checkInDate가 Null인 경우
              if (Objects.isNull(request.getCheckInDate())) {
                  errors.rejectValue("checkInDate", "NotNull", "checkInDate is null");
              }

              // checkInDate가 Null인 경우
              if (Objects.isNull(request.getCheckOutDate())) {
                  errors.rejectValue("checkOutDate", "NotNull", "checkOutDate is null");
              }

              if (request.getCheckInDate().compareTo(request.getCheckOutDate()) >= 0) {
                  errors.rejectValue("checkOutDate", "Constraint Error",
                          "checkoutdate is earlier than checkindate");
              }
          }
      }
      ```

   4. controller 수정

      ```java
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
      ```

   5. 콘솔 확인

5. **Spring Exception Handling**
   1. 오류의 종류
      - 물리적 에러(Compile Error)
        문법 잘못으로 인해 컴파일하다가 실패하는 경우
        코드를 수정해야 한다.
      - 논리적 에러(Logical Error - Algorithm Error)
        예상하지 못한 결과가 출력되는 경우
        디버깅을 통해서 로직을 수정해야 한다.
      - 예외(Exception)
        실행 중 예기치 않은 상황이 발생해서 중단되는 현상
        수정을 하거나 예외처리 코드를 추가한다.
      - 단언(Assertion)
        특정 조건을 설정해서 이 조건을 만족하지 않으면 프로그램을 강제로 중단시키는 것
   2. Java의 예외 분류
      - Checked Exception
        반드시 예외 처리를 해야 함.
        컴파일 단계에서 확인
        `IOException`이나 `SQLException`이 대표적 - 외부에서 생성한 자원을 생성하는 경우(File, Network, Database 사용 시)
      - Unchecked Exception
        예외 처리를 강제하지 않음
        런타임(실행) 단계에서 확인.
        `Runtime Exception`
   3. 처리 방법
      - 예외 복구
        try - catch - finally
      - 예외 회피
        throws를 이용해서 호출하는 곳에 예외를 전달
      - 예외 전환
        예외가 발생했을 때 catch 구문에서 다른 예외를 발생시키는 것(throw)
        사용자 정의 예외 클래스를 사용하는 것
        가장 대표적인 경우는 Java에서 SQLSyntaxException이 발생하면 Spring은 자신이 소유한 예외 클래스를 이용해서 발생시킨다.
   4. Web Programming 에서의 예외 처리
      - Web Programming에서는 예외가 발생하면 WAS(Web Application Server - Web Container)가 자신이 소유한 예외 처리 구문을 수행해서 메시지를 출력
      - 클라이언트 입장에서는 WAS가 제공하는 화면을 보는 것은 의미가 없다.
      - 별도의 예외 클래스를 만들거나 예외 처리 페이지를 만들어서 제공하는 것을 권장
   5. Spring MVC 에서는 별도의 예외 처리 클래스를 등록해서 사용
      - 모든 Controller의 예외를 한 곳에서 처리: ControllerAdvice와 ExceptionHandler 이용
      - 특정 Controller의 예외만 처리: ExceptionHandler 이용
