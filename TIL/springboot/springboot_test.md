# [Spring Boot] Test

1. **TDD(Test Driven Developmen - 테스트 주도 개발)**

   소프트웨어 개발 방법론 중 하나

   TDD가 나오기 전에는 Production(제품 기능) Code가 작성된 이후에 테스트 코드를 작성했는데 TDD에서는 테스트 코드를 먼저 작성하고 테스트를 통과하기 위해서 최소한 개선한 후 테스트를 통과한 프로덕션 코드를 리팩토링

   TDD는 테스트 기술이 아니고 분석 기술이며 설계 기술

   1. Cycle

      - RED → GREEN → REFACTOR
      - **RED**
        테스트 코드 작성 과정
        동작하는 프로덕션 코드가 없는 상황에서 테스트 코드를 먼저 작성하는 것
        프로덕션 코드가 어떤 동작을 하면 좋을지 작성
        **요구 사항을 작성하는 것과 같음**
        테스트 코드만 작성하기 때문에 에러가 발생
        핵심 동작은 테스트 코드를 작성하는 것으로 RED를 띄우는 것.

        ```java
        @Test
        void plusTest() {
        	// given - 이 일을 누가 할 것인가 - 일반적으로는 클래스
        	Calculator calculator = new Calculator();

        	// when - 어떤 일을 할 것인가
        	int result = calculator.plus(1,3);

        	// then - 기대값
        	assertThat(result).isEqualTo(4);
        }
        ```

        현재 코드는 에러가 발생 - Calculator 클래스가 존재하지 않음.

      - **Green**
        테스트를 통과하는 최소 코드를 작성
        실패하는 테스트 코드를 통과하도록 최소한의 코드를 작성
        가장 빠르게 GREEN을 보는 것이 목적
        완전한 코드를 작성해도 되는데 이것이 시간이 걸리는 일이라면 테스트를 통과하는데만 집중(죄악)
        이 과정에서는 어떤 죄악을 저질로도 된다.
        **이 과정에서는 처음에는 변수를 사용하지 않고 상수를 사용**
        ```java
        public class Calculator() {
        	public int plus(int a, int b) {
        		// 이렇게 만들지 마라 - 오래 걸리니
        		// 실제 입력을 받아서 API를 만들거나 하지 마라.
        		// 통과했을 때나 안했을 때 나누어서 JSON을 던지던지 하자.
        		// return (a + b);
        		return 4;
        	}
        }
        ```
      - **REFACTOR**
        GREEN을 통과하기 저질렀던 죄악을 수습하는 과정
        좋은 코드로 리팩토링
        ```java
        public class Calculator() {
        	public int plus(int a, int b) {
        		return (a + b);
        	}
        }
        ```

   2. TDD 원칙
      - 실패하는 단위 테스트를 작성하기 전에는 프로덕션 코드를 작성하지 않음.
      - 컴파일은 실패하지 않으면서 실행이 실패하는 정도로만 단위 테스트를 작성
      - 현재 실패하는 테스트를 통과할 정도로만 실제 코드를 작성.
   3. 필요한 이유
      - 변화에 대한 불안감 해서
        프로덕션에 대한 테스트 코드가 이미 작성되어 있으므로 변화에 대한 영향력을 빠르게 파악할 수 있음.
        불안감을 지루함으로 바꾸는 작업
      - 한 번에 하나의 일에만 집중할 수 있음.
        Controller 작업을 하다가 Service 작업을 다시 수행한다면 이것은 방법론적인 측면에서 개발의 실패
        야크털 깎기를 하지 않을 수 있음.
      - 빠른 피드백
        버그를 찾는게 빨라짐.
        처음부터 완벽한 설계를 하지 않고 점진적인 설게를 해나갈 수 있음.
      - 테스트 코드 자체가 문서화
        **테스트 코드가 요구사항이기 때문**
      - 테스트를 나중에 작성하는 것은 귀찮음
      - 코드 퀄리티
        테스트 주도 개발을 하면 의존성을 낮출 수 있기 때문에 모듈 간 결합도를 낮추고 응집도를 높일 수 있음 - **Filter**, **Interceptor**, **AoP**, **구독과 게시**를 적절히 이용하면 의존성을 낮출 수 있음.

2. **테스트 종류**
   1. **Unit Test**
      - 가장 작은 단위의 테스트
      - 함수나 메서드 또는 클래스 단위로 테스트
      - 의도한 결과가 나오는지 테스트
   2. **Integration Test**
      - 모듈을 통합하는 과정에서 발생하는 호환성 문제
      - 단위 테스트보다 테스트 비용이 커지게 된다.
   3. **Regression Test**
      - 버그가 발생한 입력 조건 값을 사용해서 정상 동작하도록 테스트 케이스를 작성
   4. **System Teest**
      - 시스템이 전부 만들어진 상태에서 전체 시스템을 테스트
      - 수행 시간, 파일 저장 및 처리 능력, 최대 부하, 복구 및 재시동 능력, 수작업 절차 등을 테스트
      - **이 테스트 이전 단계까지 MSA에서 파드를 몇개 띄울거다 결정하는 것은 의미가 없다.** 이 시스템 테스트를 통해서 테스트를 하고 파드같은 것을 띄우게 된다.
   5. **Acceptance Test** - 인수 테스트
      - 기획자나 클라이언트의 시나리오를 만들고 테스트
      - 애자일 방법론 중 하나인 XP(익스트림 프로그래밍)에서 제안
   6. 서비스를 테스트
      - 알파 테스트
        개발자의 장소에서 고객이 테스트
      - 베타 테스트
        고객의 장소에서 고객이 테스트
   7. Black Box Test와 White Box Test
      - Black Box Test: 기능을 테스트
      - White Box Test: 내부 구조를 테스트
3. **테스트 코드 작성 방법**

   1. **Given-When-Then**
      - Given
        테스트를 하기 전에 환경 설정을 하는 과정
      - When
        테스트의 목적을 보여주는 과정
      - Then
        테스트의 결과를 검증하는 과정
      - **BDD**(Behavior Driven Development - 행위 주도 개발)에서 탄생한 테스트 접근 방식
        인수 테스트 과정에서 필요
   2. Given-When-Then 사례

      - HashSet은 중복된 데이터를 저장하지 않는 자료구조

        ```java
        // Given
        Integer value = Integer.valueOf(3);
        Set<Integer> set = new HashSet<>();

        // When
        set.add(value);
        set.add(value);

        // Then
        // System.out.println(set.size()); - 테스트는 출력하는 것이 아니다.
        Assertion.assertEquals(1, set.size());
        ```

   3. 좋은 테스트 코드를 작성하는 5가지 성질 - FIRST
      - Fast: 빠르게 동작
      - Isolated, Elated: 독립적으로 수행
      - Repeatable: 반복적으로 사용 가능
      - Self-Validating: 자가 검증
      - Timely: 적시에
   4. 테스트 코드 작성
      - **MSA 환경에서는 테스트 케이스가 더욱 중요**
        여러 작은 컴포넌트가 서로 연동해서 서비스를 제공하는 특징을 갖기 때문
      - **CI/CD 측면에서도 테스트는 필수 - 테스트 커버리지가 더욱 높아진다.**
      - 중요한 메서드나 사용 빈도가 높은 클래스는 다양한 형태의 테스트를 추가 및 확장하는 것이 중요

4. **Spring Boot Test**

   1. 테스트를 위한 스타터를 제공: spring-boot-starter-test

      프로젝트를 생성하면 자동 주입

   2. 제공되는 라이브러리
      - JUnit
      - Mockito
        가짜 객체를 만들 수 있는 라이브러리
        Repository → Service → Controller를 순차적으로 만들면서 테스트를 할 때는 이미 클래스가 만들어져 있으므로 Mockito가 불필요했다.
        Repository와 Service와 Controller를 동시 개발하고자 하는 경우 Service는 Repository가 있어야 작업이 가능하고 Controller는 Service가 있어야 작업이 가능한데 이런 경우 Service의 모양만 만들고 가짜 객체를 만들어서 테스트가 가능한데 이 떄 사용되는 라이브러리가 Mockito이고 이러한 가짜 객체를 Mock 객체라고 한다.
      - Hamcrest
        JUnit과 유사한 기능 제공
      - sprint-test
        spring framework의 테스트
        sprint-boot-test
        sprint-boot-test-autoconfiguration
   3. JUnit
      - 자바 진영에서 사용하는 대표적인 테스트 프레임워크로 단위 테스트와 통합 테스트 기능을 제공
      - 어노테이션 만으로 설정해서 사용할 수 있어서 간편하게 테스트 코드를 만들 수 있음
      - 테스트 메서드 위에 @Test를 추가하면 테스트를 위한 메서드를 생성할 수 있음.
        **메서드의 접근 지정자는 public이어야 하고 리턴 타입은 void**
      - JUnit Test의 Life Cycle
        - @BeforeAll
          테스트 메서드가 수행되기 직전에 한 번 호출
        - @BeforeEach
          모든 테스트 메서드가 수행되기 직전에 호출
        - @AfterAll
          테스트가 종료한 후 한 번 호출
        - @AfterEach
          각 테스트 메서드가 종료될 때 마다 호출
      - **Assertions 클래스**: 검증 메서드를 제공하는 클래스
        - 모든 메서드가 static
        - 검증 메서드 이름은 assert로 시작
        - 검증에 실패하면 AssertionFailedError 예외가 발생
        - 검증하는 메서드의 매개변수 이름이 expect이면 예상 값이고 actual은 실제 값이다.
        - 매개변수의 순서는 변경해도 되지만 변경하면 정확한 결과를 판단하기 어렵기 때문에 순서대로 대입하기를 권장
        - assertNull(Object): null 여부 확인
        - assertNotNull(Object): null이 아닌지 여부 확인
        - assertTrue(boolean)
        - assertFalse(boolean)
        - assertEquals(Object expect, Object actual): equals로 비교
        - assertNotEquals(Object expect, Object actual)
        - assertSame(Object expect, Object actual): == 로 비교
        - assertNotSame(Object expect, Object actual):
        - assertThrow(Class <T> expectedType, Executable executable): 예외 발생 여부를 확인해주는 메서드로 첫 번쨰 매개변수는 발생할 가능성이 있는 예외 종류이고 뒤의 내용은 앞에 매개변수와 비교해서 확인하기 위한 인터페이스
        - Assertions.assertThrows(IOException.class, () → IOUtils.copy(input, output));
          IOUtils.copy 메서드를 호출했을 때 IOException 예외가 발생하는지 여부를 확인

5. **프로젝트 실습 - Springtest0330**

   1. JUnit Test를 위한 클래스를 생성 - test.MiscTest

      ```java
      package com.gyumin.springtest0330;

      import org.junit.jupiter.api.Assertions;
      import org.junit.jupiter.api.Test;

      import java.util.HashSet;

      public class MiscTest {
          // HashSet은 중복된 데이터를 저장하지 않음.
          // 중복된 데이터를 삽입하더라도 HashSet의 size는 변경이 되면 안됨. - 시나리오
          @Test
          public void testHashSet() {
              // Given - 환경설정
              HashSet<Integer> set = new HashSet<>();
              int x = 3;
              int y = 3;

              // When - 테스트 하기 위한 작업 수행
              set.add(x);
              set.add(y);

              // Then
              // 에상값과 실제 숳애한 값이 맞는지 확인
              Assertions.assertEquals(1, set.size());
          }
      }
      ```

      **가장 위에 @SpringBootTest를 붙이지 않았는데 붙이게 되면 컨테이너를 실행하겠다는 의미가 된다. 따라서 내부 로직테스트를 할 때는 붙일 필요가 없다.**

   2. SpringBootTest
      - **내장 컨테이너**(Spring Boot가 가지고 있는 컨테이너)를 이용해서 테스트
      - **Container**
        프레임워크가 생성한 객체들을 관리하기 위한 정보 모음으로 Spring에서는 **ApplicationContext**라는 이름으로 사용
      - POJO 클래스는 우리가 new 라는 키워드를 이용해서 직접 인스턴스를 생성해서 테스트가 가능한데 Spring의 Bean은 직접 생성해서 테스트할 수 있지만 다른 인스턴스의 의존성을 주입받는 경우에는 **new를 이용해서 직접 생성하면 안됨.**
      - Spring의 Bean은 ApplicationContext의 정보를 이용해서 테스트해야 함.
        이런 경우 상단에 @SpringBootTest를 추가하면 Spring Container에 생성된 Bean을 가지고 테스트가 가능.
   3. @SpringBootTest를 이용한 Service테스트

      Entity, Repository, DTO - RequestDTO, ResponseDTO, Service, Controller, FIlter, Util

      - 요청 DTO 클래스 생성 - HotelRequest
        DTO 클래스는 별도의 패키지를 만들어서 저장하기도 하고(Micro Service) controller 패키지에 넣기도 한다.

        ```java
        package dto;

        import lombok.Getter;
        import lombok.ToString;

        @Getter
        @ToString
        public class HotelRequestDTO {
            private String hotelName;

            public HotelRequestDTO() {

            }

            public HotelRequestDTO(String hotelName) {
                this.hotelName = hotelName;
            }
        }
        ```

      - 응답 DTO 클래스 생성 - HotelResponseDTO

        ```java
        package dto;

        import lombok.Getter;
        import lombok.ToString;

        @Getter
        @ToString
        public class HotelResponseDTO {
            private Long hotelId;
            private String hotelName;
            private String address;
            private String phoneNumber;

            public HotelResponseDTO(Long hotelId, String hotelName, String address, String phoneNumber) {
                this.hotelId = hotelId;
                this.hotelName = hotelName;
                this.address = address;
                this.phoneNumber = phoneNumber;
            }
        }
        ```

      - 서비스 인터페이스 생성 - service.DisplayService

        ```java
        package service;

        import dto.HotelRequestDTO;
        import dto.HotelResponseDTO;

        import java.util.List;

        public interface DisplayService {
            List<HotelResponseDTO> getHotelsByName(HotelRequestDTO hotelRequestDTO);
        }
        ```

      - 서비스 클래스 생성 - service.DisplayServiceImpl

        ```java
        package service;

        import dto.HotelRequestDTO;
        import dto.HotelResponseDTO;
        import lombok.extern.log4j.Log4j2;
        import org.springframework.stereotype.Service;

        import java.util.List;
        import java.util.concurrent.TimeUnit;

        @Log4j2
        @Service
        public class DisplayServiceImpl implements DisplayService{
            @Override
            public List<HotelResponseDTO> getHotelsByName(HotelRequestDTO hotelRequestDTO) {
                try {
                    TimeUnit.SECONDS.sleep(5);
                } catch (InterruptedException e) {
                    log.error("error", e.getMessage());
                }
                return List.of(new HotelResponseDTO(1000L, "메리어트", "서울시", "188292"));
            }
        }
        ```

6. **@SpringBootTest 클래스 구조**

   ```java
   package org.springframework.boot.test.context;

   @Target({ElementType.TYPE})
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   @Inherited
   @BootstrapWith(SpringBootTestContextBootstrapper.class)
   @ExtendWith({SpringExtension.class})
   public @interface SpringBootTest {
     @AliasFor("properties")
     String[] value() default {};

     @AliasFor(”value")
     String[] properties() default {};
    
     Class<?>[] classes() default {};

     String!] args() default {};

     SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.MOCK;
   }
   ```

   - value나 properties는 테스트 과정에서 사용할 값을 설정할 수 있는 속성
   - classes는 설정 관련된 클래스들을 로딩하기 위한 속성 - 기본은 ApplicationContext의 모든 빈 객체를 로딩
   - args는 실행할 때의 매개변수
   - MOCK은 실제 서블릿 컨테이너를 이용하지 않고 웹 테스트를 진행
   - SpringBootTest 설정 예시
     ```java
     @SpringBootTest(properties={"search.host=127.0.0.1", "search.port=19999"}
     ```
     위의 내용이 properties 파일에 존재한다면 다음와 같이 가능
     ```java
     @SpringBootTest(properties={"spring.config.location=classpath:설정파일경로"})
     ```
     로 설정해서 설정 파일의 내용을 전부 읽어서 테스트가 가능
     기본이 application.properties와 application.yml이다.
   - **개발 환경과 운영 환경 그리고 테스트 환경**
     운영 환경은 RDS에 하고 싶은데, 개발을 할 때도 RDS를 사용?
     테스트를 할 때도 RDS를 사용?
     **환경 마다 다른 설정을 사용하는 경우 환경 설정을 변경해가면서 하는 것은 바람직하지 않다.**
     설정이나 코드를 변경하게 되면 예기치 않은 오류가 발생할 수 있기 때문이다.
     설정 파일을 분리해서 사용하는 것을 권장한다.
     application.properties는 운영 환경에 맞춰 설정
     application-test.properties는 테스트 환경에 맞춰 설정

7. **@TestConfiguration**
   - 테스트 케이스를 작성할 떄 대상 클래스의 기능만 테스트할 수 있도록 범위를 좁히는 것이 중요
     다른 클래스에 의존을 하게 되면 기능을 정확하게 테스트할 수 없는 경우가 많다.
   - Service를 테스트할 떄 Repository를 실제 환경에서 테스트하게 되면 불필요한 데이터가 삽입도이ㅓ서 정확한 테스트를 하는 것이 곤란한 경우가 있다.
     원본 데이터에 훼손이 생김.
     **데이터베이스 작업을 할 때는 H2와 같은 내무 메모리 데이터베이스를 이요해서 테스트하고 개발이나 운영할 때 실제 데이터베이스를 이요하는 것을 권장**
8. **실습 이어서 진행**

   1. Entity 클래스 생성 - domain.HotelRoomEntity

      ```java
      package com.gyumin.springtest0330.domain;

      import lombok.Getter;
      import lombok.ToString;

      @Getter
      @ToString
      // @Entity - 처음에는 안붙인다 - 왜냐면 붙이면 바로 DB 연동이 되고 쓸데없는 데이터가 들어갈 수 있다.
      // 그러니까 테스트가 끝나고 붙여도 된다.
      public class HotelRoomEntity {
          private Long id;
          private String code;
          private Integer floor;
          private Integer bedCount;
          private Integer bathCount;
      }
      ```

   2. 응답 객체 생성 - dto.HotelRoomResponseDTO

      ```java
      package com.gyumin.springtest0330.dto;

      import com.gyumin.springtest0330.domain.HotelRoomEntity;
      import lombok.Getter;

      @Getter
      public class HotelRoomResponseDTO {

          private Long hotelRoomId;
          private String code;
          private Integer floor;
          private Integer bedCount;
          private Integer bathCount;

          public HotelRoomResponseDTO(Long hotelRoomId, String code, Integer floor, Integer bedCount, Integer bathCount) {
              this.hotelRoomId = hotelRoomId;
              this.code = code;
              this.floor = floor;
              this.bedCount = bedCount;
              this.bathCount = bathCount;
          }

          // Entity를 DTO로 변환하는 메서드
          public static HotelRoomResponseDTO from(HotelRoomEntity entity) {
              return new HotelRoomResponseDTO(
                      entity.getId(), entity.getCode(), entity.getFloor(), entity.getBedCount(), entity.getBathCount()
              );
          }
      }
      ```

   3. Repository 클래스나 인터페이스 생성 - repository.HotelRoomRepository

      ```java
      package com.gyumin.springtest0330.persistence;

      import com.gyumin.springtest0330.domain.HotelRoomEntity;
      import org.springframework.stereotype.Repository;

      @Repository
      public class HotelRoomRepository {
          // 더미데이터를 만들면 테스트가 가능
          public HotelRoomEntity findById(Long id) {
              return new HotelRoomEntity(id, "EAST_1999", 20, 2, 1);
          }
      }
      ```

   4. 테스트할 서비스 클래스 생성 - service.HotelRoomDisplayService

      ```java
      package com.gyumin.springtest0330.service;

      import com.gyumin.springtest0330.domain.HotelRoomEntity;
      import com.gyumin.springtest0330.dto.HotelRoomResponseDTO;
      import com.gyumin.springtest0330.persistence.HotelRoomRepository;
      import lombok.RequiredArgsConstructor;
      import lombok.extern.log4j.Log4j2;
      import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
      import org.springframework.stereotype.Service;

      @Log4j2
      @Service
      public class HotelRoomDisplayService {
          private final HotelRoomRepository hotelRoomRepository;
          private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

          public HotelRoomDisplayService(HotelRoomRepository hotelRoomRepository, ThreadPoolTaskExecutor threadPoolTaskExecutor) {
              this.hotelRoomRepository = hotelRoomRepository;
              this.threadPoolTaskExecutor = threadPoolTaskExecutor;
          }

          // 테스트할 메서드
          public HotelRoomResponseDTO getHotelRoomById(Long id) {
              HotelRoomEntity hotelRoomEntity = hotelRoomRepository.findById(id);
              // 스레드 풀을 이용해서 스레드 생성 및 수행
              threadPoolTaskExecutor.execute(() -> log.warn("entity:{}", hotelRoomEntity.toString()));

              return HotelRoomResponseDTO.from(hotelRoomEntity);
          }
      }
      ```

   5. 개발 및 운영환경에서 사용할 스레드 풀 클래스 - config.ThreadPoolConfig

      ```java
      package com.gyumin.springtest0330.config;

      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;
      import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

      @Configuration
      public class ThreadPoolConfig {
          // 빈이 파괴될 때 shutdown 메서드를 호출하겠다.
          @Bean(destroyMethod = "shutdown")
          public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
              ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
              // 스레드 풀에 만들어지는 스레드의 최대 개수
              // 이 개수가 코어의 개수보다 많으면 성능이 저하될 위험성이 있다.
              taskExecutor.setMaxPoolSize(Runtime.getRuntime().availableProcessors());
              taskExecutor.setThreadNamePrefix("AsyncExecutor-");
              taskExecutor.afterPropertiesSet();
              return taskExecutor;
          }
      }
      ```

   6. 테스트 환경에서 사용할 스레드 풀 클래스를 별도로 생성 - test.TestThreadPoolConfig

      ```java
      package com.gyumin.springtest0330;

      import org.springframework.boot.test.context.TestConfiguration;
      import org.springframework.context.annotation.Bean;
      import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

      @TestConfiguration
      public class TestThreadPoolConfig {
          // 빈이 파괴될 때 shutdown 메서드를 호출하겠다.
          @Bean(destroyMethod = "shutdown")
          public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
              ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
              // 스레드 풀에 만들어지는 스레드의 최대 개수
              taskExecutor.setMaxPoolSize(1);
              taskExecutor.setThreadNamePrefix("AsyncExecutor-");
              taskExecutor.afterPropertiesSet();
              return taskExecutor;
          }
      }
      ```

      **근데 지금 두 곳에서 스레드 풀을 만들고 있음.**

   7. 테스트에서 사용할 설정 파일을 만들고 추가 - application.test-properties

      **동일한 자료형의 bean이 2개 이상 만들어지더라도 에러를 발생시키지 않도록 하는 옵션**

      ```yaml
      spring.main.allow-bean-definition-overriding=true
      ```

   8. 테스트 클래스를 작성 - test.HotelRoomDisplayServiceTests

      ```java
      package com.gyumin.springtest0330;

      import org.springframework.boot.test.context.TestConfiguration;
      import org.springframework.context.annotation.Bean;
      import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

      @TestConfiguration
      public class TestThreadPoolConfig {
          // 빈이 파괴될 때 shutdown 메서드를 호출하겠다.
          @Bean(destroyMethod = "shutdown")
          public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
              ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
              // 스레드 풀에 만들어지는 스레드의 최대 개수
              taskExecutor.setMaxPoolSize(1);
              taskExecutor.setThreadNamePrefix("TestExecutor-");
              taskExecutor.afterPropertiesSet();
              return taskExecutor;
          }
      }
      ```

9. **MockBean**

   Mockito는 일종의 모의 객체라고 할 수 있는 Mock 객체를 생성하고 검증할 수 있도록 해주는 메서드를 제공하는 라이브러리

   1. HotelRoomRepository를 인터페이스로 바꾸고 메서드를 구현하지 않은 상태로 변경

      **인터페이스이므로 메서드가 구현되지도 않았고 인스턴스를 생성할 수 없음 → 근데 테스트를 하고 싶다!**

      ```java
      package com.gyumin.springtest0330.persistence;

      import com.gyumin.springtest0330.domain.HotelRoomEntity;
      import org.springframework.stereotype.Repository;

      @Repository
      public Interface HotelRoomRepository {
      		/*
          public HotelRoomEntity findById(Long id) {
              return new HotelRoomEntity(id, "EAST_1999", 20, 2, 1);
          }
      		*/
      		public HotelRoomEntity findById(Long id)
      }
      ```

   2. test 클래스를 작성하고 실행 - test.HotelRoomDisplayService2

      ```java
      package com.gyumin.springtest0330;

      import com.gyumin.springtest0330.domain.HotelRoomEntity;
      import com.gyumin.springtest0330.dto.HotelRoomResponseDTO;
      import com.gyumin.springtest0330.persistence.HotelRoomRepository;
      import com.gyumin.springtest0330.service.HotelRoomDisplayService;
      import org.junit.jupiter.api.Assertions;
      import org.junit.jupiter.api.Test;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.context.SpringBootTest;
      import org.springframework.boot.test.mock.mockito.MockBean;

      // static import
      // 클래스의 특정 static 멤버만 import
      // 가독성을 높이기 위해서 수행
      import static org.mockito.ArgumentMatchers.any;
      import static org.mockito.BDDMockito.given;

      @SpringBootTest
      public class HotelRoomDisplayServiceTest2 {
          @Autowired
          private HotelRoomDisplayService hotelRoomDisplayService;

          // 가짜로 객체 만들기
          @MockBean
          private HotelRoomRepository hotelRoomRepository;

          @Test
          public void testMockBean() {
              // getHotelRoomById가 지금 구현되있긴 한데,
              // 실제로 구현이 안된 메서드를 호출해서 결과를 만들어 낼 수 있다.
              // HotelRoomRepository를 Interface로 바꾸고 메서드를 public HotelRoomEntity findById(Long id) 라고만 바꿔보자.
              // 그리고 아래를 수행하면 수행이 잘 된다.
              // any()는 아무것이나 대입하면 결과를 만들어주겠다는 의미이다.
              // 이렇게 가짜로 메서드를 수행하는 경우 이 메서드를 stub이라고 한다.
              given(this.hotelRoomRepository.findById(any()))
                      .willReturn(new HotelRoomEntity(10L, "test", 1, 1, 1));

              HotelRoomResponseDTO hotelRoomResponseDTO = hotelRoomDisplayService.getHotelRoomById(1L);
              Assertions.assertNotNull(hotelRoomResponseDTO);
              Assertions.assertEquals(10L, hotelRoomResponseDTO.getHotelRoomId());
          }
      }
      ```

10. **Test Slice Annotation**
    - 테스트를 할 때 모든 Bean을 전부 만들어서 테스트를 하게 되면 자원의 낭비도 많고 시간이 너무 많이 걸릴 수 있음. **클라이언트의 요청을 받아서 Controller가 제대로 응답하는지 확인(관심 대상)**하고자 하는 경우 Repository와 Service가 이미 구현되어 있다면 테스트할 때 **전부 호출(관심 대상 아님**)할지도 모른다.
    - @WebMvcTest
      - @Controller, @RestController, @ControllerAdvice, Converter, Filter, WebMvcConfigurer만 동작
      - @Service, @Repository, @Component는 동작하지 않음.
    - @DataJpaTest
      - @Repository와 JPA에서 사용하는 기능만 동작
      - @Service, @Component, @Controller는 동작하지 않음.
    - @JsonTest
      - JSON 직렬화와 역직렬화만 테스트
    - @RestClientTest
      - RestController 관련된 기능만 동작
    - @DataMongoTest
      - MongoDB 테스트
11. **Spring Boot Web MVC Test**

    - 서버와 클라이언트 네트워크 구간은 문제가 없다고 가정하고 서버 애플리케이션의 요청, 응답 기능이 정상적으로 동작하는지 확인 - **즉 서버를 키고 POSTMAN을 켜서 확인할 필요가 없다.**
    - REST-API 요청 조건에 따라 Controller 클래스가 어떤 형식의 JSON 응답 메시지를 리턴하는지 테스트
    - Controller 생성 - controller.HotelController

      ```java
      package com.gyumin.springtest0330.controller;

      import com.gyumin.springtest0330.domain.HotelRoomEntity;
      import com.gyumin.springtest0330.dto.HotelRequestDTO;
      import com.gyumin.springtest0330.dto.HotelResponseDTO;
      import com.gyumin.springtest0330.dto.HotelRoomResponseDTO;
      import com.gyumin.springtest0330.service.DisplayService;
      import com.gyumin.springtest0330.service.HotelRoomDisplayService;
      import lombok.RequiredArgsConstructor;
      import lombok.extern.log4j.Log4j2;
      import org.springframework.http.ResponseEntity;
      import org.springframework.web.bind.annotation.PostMapping;
      import org.springframework.web.bind.annotation.RequestBody;
      import org.springframework.web.bind.annotation.ResponseBody;
      import org.springframework.web.bind.annotation.RestController;

      import java.util.List;

      @Log4j2
      @RestController
      @RequiredArgsConstructor
      public class HotelController {
          private final DisplayService displayService;

          @ResponseBody
          @PostMapping("/hotels/fetch-by-name")
          public ResponseEntity<List<HotelResponseDTO>> getHotelByName(@RequestBody HotelRequestDTO hotelRequestDTO) {
              List<HotelResponseDTO> hotelResponses = displayService.getHotelsByName(hotelRequestDTO);
              return ResponseEntity.ok(hotelResponses);
          }

      }
      ```

    - Mapper 클래스 생성 - util.JsonUtil

      ```java
      package com.gyumin.springtest0330.util;

      import com.fasterxml.jackson.databind.ObjectMapper;

      public class JsonUtil {
          public static final ObjectMapper objectMapper = new ObjectMapper();
      }
      ```

    - test 클래스 생성 - test.ApiControllerTest01

      ```java
      package com.gyumin.springtest0330;

      import com.gyumin.springtest0330.dto.HotelRequestDTO;
      import com.gyumin.springtest0330.util.JsonUtil;
      import org.hamcrest.Matchers;
      import org.junit.jupiter.api.Test;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
      import org.springframework.boot.test.context.SpringBootTest;
      import org.springframework.http.MediaType;
      import org.springframework.test.web.servlet.MockMvc;
      import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

      import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
      import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

      // 가짜 웹 환경을 만듬.
      @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
      @AutoConfigureMockMvc
      public class ApiControllerTest01 {
          @Autowired
          private MockMvc mockMvc;

          @Test
          public void testGetHotelById() throws Exception {
              // RequestBody 생성
              HotelRequestDTO hotelRequestDTO = new HotelRequestDTO("Ragged Point");

              // 객체의 내용을 Json 문자열로 변경
              String jsonRequest = JsonUtil.objectMapper.writeValueAsString(hotelRequestDTO);

              // 요청 처리 생성
              mockMvc.perform(post("/hotels/fetch-by-name")
                              .content(jsonRequest)
                              .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isOk())
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(content().string("[{\"hotelId\":1000,\"hotelName\":\"Ragged Point Inn\",\"address\":\"18091 CA-1, San Simeon, CA 93452\",\"phoneNumber\":\"+18885846374\"}]"))
                      .andExpect(content().json("[{\"hotelId\":1000,\"hotelName\":\"Ragged Point Inn\",\"address\":\"18091 CA-1, San Simeon, CA 93452\",\"phoneNumber\":\"+18885846374\"}]"))
                      .andExpect(jsonPath("$[0].hotelId", Matchers.is(1000)))
                      .andExpect(jsonPath("$[0].hotelName", Matchers.is("Ragged Point Inn")))
                      .andDo(MockMvcResultHandlers.print(System.out));
          }
      }
      ```

      **응답과 결과를 직접 JSON 문자열로 만들어야 하기 때문에 테스트 도구를 사용하는 것보다 불편함**

    - Test 클래스를 수정

      ```java
      package com.gyumin.springtest0330;

      import com.gyumin.springtest0330.controller.HotelController;
      import com.gyumin.springtest0330.dto.HotelRequestDTO;
      import com.gyumin.springtest0330.dto.HotelResponseDTO;
      import com.gyumin.springtest0330.service.DisplayService;
      import com.gyumin.springtest0330.util.JsonUtil;
      import org.hamcrest.Matchers;
      import org.junit.jupiter.api.BeforeEach;
      import org.junit.jupiter.api.Test;
      import org.mockito.invocation.InvocationOnMock;
      import org.mockito.stubbing.Answer;
      import org.springframework.beans.factory.annotation.Autowired;
      import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
      import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
      import org.springframework.boot.test.context.SpringBootTest;
      import org.springframework.boot.test.mock.mockito.MockBean;
      import org.springframework.http.MediaType;
      import org.springframework.test.web.servlet.MockMvc;
      import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

      import java.util.List;

      import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
      import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

      import static org.mockito.ArgumentMatchers.any;
      import static org.mockito.BDDMockito.given;

      // 가짜 웹 환경을 만듬.
      @WebMvcTest(controllers = HotelController.class)
      @AutoConfigureMockMvc
      public class ApiControllerTest01 {
          @Autowired
          private MockMvc mockMvc;

          @MockBean
          private DisplayService displayService;

          // 테스트 메서드가 수행되기 전에 호출
          @BeforeEach
          public void init() {
              given(displayService.getHotelsByName(any()))
                      .willAnswer(new Answer<List<HotelResponseDTO>>() {
                          @Override
                          public List<HotelResponseDTO> answer(InvocationOnMock invocation) throws Throwable {
                              HotelRequestDTO hotelRequestDTO = invocation.getArgument(0);
                              return List.of(new HotelResponseDTO(1L, hotelRequestDTO.getHotelName(), "unknown", "213"));
                          }
                      });
          }

          @Test
          public void testGetHotelById() throws Exception {
              // RequestBody 생성
              HotelRequestDTO hotelRequestDTO = new HotelRequestDTO("Ragged Point");

              // 객체의 내용을 Json 문자열로 변경
              String jsonRequest = JsonUtil.objectMapper.writeValueAsString(hotelRequestDTO);

              // 요청 처리 생성
              mockMvc.perform(post("/hotels/fetch-by-name")
                              .content(jsonRequest)
                              .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isOk())
                      .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                      .andExpect(jsonPath("$[0].hotelId", Matchers.is(1000)))
                      .andExpect(jsonPath("$[0].hotelName", Matchers.is("Ragged Point")))
                      .andDo(MockMvcResultHandlers.print(System.out));
          }
      }
      ```
