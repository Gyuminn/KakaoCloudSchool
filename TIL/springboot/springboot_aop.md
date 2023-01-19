# [Spring Boot] AOP

1. **AOP(Aspect Oriented Programmng - 관점 지향 프로그래밍)**

   객체 지향 프로그래밍을 보완하는 개념으로 메서드나 클래스를 관점에 따라 분리시켜서 구현하는 프로그래밍 방법.

   MVC Pattern의 경우에는 데이터를 다루는 Repository, Service, View, Controller로 구분

   Repository와 Service를 Model로 설정

   Service 부분을 살펴보면 업무 로직을 처리하기 위한 부분과 업무 로직을 처리하기 위해서 필요한 부분 또는 업무와는 상관없지만 필요한 코드를 같이 작성을 하게 된다.

   실제 업무에 관련된 로직을 Business Logic이라고 하고 다른 내용은 Cross Cutting Concern(공통 관심 사항)이라고 하고 이를 분리해서 프로그래밍하는 것이 AOP이다.

   위와 같이 프로그래밍을 하되 Java Web Programming에서는 직접 인스턴스를 생성하거나 메서드를 호출하지 않고 코드를 작성한 후 설정을 하면 컴파일할 때 또는 빌드할 때 또는 실행할 때 코드를 조합하는 형태로 수행한다.

2. **AOP 구현 방법**
   1. Filter
      - Java EE의 Spec
      - Spring과는 상관이 없어서 Filter를 사용하면 Spring의 Bean 조작이 안됨.
      - 인코딩이나 XSS(Cross-Site Scripting) 방어에 주로 이용
   2. Spring Interceptor
      - URI 요청 및 응답 시점을 가로채서 전/후 처리를 수행
      - **Controller가 처리하기 전이나 후의 작업을 작성**
   3. Spring AOP
      - 메서드 호출 전 후 처리를 수행
      - **Busniess Logic 수행 전 후 처리 - Service에서 사용**
3. **HandlerInterceptor**

   인터셉터를 구현하기 위한 인터페이스

   1. 메서드
      - preHandle
        - Controller가 처리하기 전에 호출되는 메서드
        - 여기서 true를 리턴하면 Controller로 이동하고 false를 리턴하면 Controller로 이동하지 않음.
      - postHandle
        - Controller가 처리한 후에 호출되는 메서드
        - 예외가 발생하면 호출되지 않음.
      - afterCompletion
        - Controller가 처리한 후에 무조건 호출되는 메서드
   2. 설정
      - WebMvcConfigure 인터페이스를 구현한 클래스의 addInterceptors를 재정의
        (Spring MVC의 web.xml 역할을 한다.)

4. **이전 프로젝트에서 수행**

   1. 인터셉터 클래스를 생성 - aop.MeasuringInterceoptor

      ```java
      @Log4j2
      public class MeasuringInterceptor implements HandlerInterceptor {
          // Controller에게 요청을 하기 전에 호출되는 메서드
          // false로 리턴하면 Controller에게 요청을 전달하지 않음.
          @Override
          public boolean preHandle(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Object handler) {
              log.warn("Controller가 요청을 처리하기 전에 호출");

              return true;
          }

          // Controller가 요청을 정상적으로 처리한 후 호출되는 메서드
          @Override
          public void postHandle(HttpServletRequest request,
                                 HttpServletResponse response,
                                 Object handler,
                                 ModelAndView modelAndView) {
              // 로그 기록
              log.warn("요청을 정상적으로 처리한 후 호출");
          }

          // Controller가 요청을 처리한 후 무조건 호출되는 메서드
          @Override
          public void afterCompletion(HttpServletRequest request,
                                      HttpServletResponse response,
                                      Object handler,
                                      Exception e) {

              log.warn("비정상적으로 처리되도 호출");
          }
      }
      ```

   2. 프로젝트에 Web Project 설정을 위한 클래스를 추가하고 인터셉터 적용 - config.WebMvcConfig

      ```java
      // 웹 설정 클래스
      @Configuration
      public class WebMvcConfig implements WebMvcConfigurer {
          // 인터셉터 설정 메서드
          @Override
          public void addInterceptors(InterceptorRegistry registry) {
              registry.addInterceptor(new MeasuringInterceptor())
                      .addPathPatterns("/user/"); // 인터셉터가 적용될 URL
          }
      }
      ```

   3. Controller 클래스에 요청 처리 메서드

      ```java
      @Controller
      @Log4j2
      public class HomeController {
          @GetMapping("/")
          public String home() {
              return "redirect:/movie/list";
          }

          @GetMapping("/user")
          public String user() {
      				// 확인용
              return "생략";
          }
      }
      ```

   4. 프로젝트를 실행하고 URL을 입력해서 요청을 생성한 후 콘솔을 확인

5. **AOP**

   1. AOP 필요성

      웹 애플리케이션 개발을 할 때 여러 계층으로 나누어서 구현

      여러 계층에 비지니스 로직과 관련이 없는 공통적인 로직이 필요한 경우가 있는데 이러한 로직을 Cross-Cutting Concern이라고 하는데 이러한 코드를 각 계층에 구현하면 코드를 유지보수하는게 어렵다.

   2. AOP 장점

      비즈니스 모듈에는 주요 관심사에 대한 로직만 존재

      비즈니스 모듈을 수정하지 않고도 추가 동작을 작성할 수 있음.

   3. 구현 방식

      Proxy 패턴을 이용해서 구현

      외부에서 비즈니스 로직을 가진 객체(Target)을 호출하면 이 객체를 감싸고 있는 외부 객체(Proxy)를 호출해서 Target에게 전달하는 방식

      Proxy는 구현이 될 때 Target을 상속받아서 만들어지기 때문에 Target과 동일한 방식으로 호출하는 것이 가능

      ```java
      class Target{
      }
      class Proxy extends Target{
      }
      Target target = new Proxy();
      ```

   4. **AOP 용어**(중요한데 아는 사람이 별로 없대)

      - Advice
        - 공통 기능의 코드로 로그 출력이나 트랜잭션 관리 등
        - 언제 적용할 것(Before, After, Around, After Returning, After Throwing, Introduction)인지를 설정해서 사용
      - JoinPoints
        - advicer가 적용 가능한 지점
        - 메서드 호출 전 후와, 속성 호출 전 후가 가능한데 스프링은 메서드 호출 전 후만 가능
      - PointCut
        - advice와 joinpoints를 결합하기 위한 설정
        - 정규 표현식이나 패턴을 이용하는데 스프링에서는 AspectJ pointcut 표현 언어를 사용
      - Weaving
        - Advice와 Target을 결합하는 시점으로 컴파일 시, 클래스 로딩 시, 런타임 시 등 3가지가 있음.
      - Target
        - Advice가 적용되는 객체
      - Aspect
        - 공통 관심사항과 이를 적용하는 코드 상의 포인트를 모은 것

      | AOP | OOP |
      | --- | --- |

      | Aspect
      포인트컷, 어드바이스 및 속성을 캡슐화하는 코드 단위 | Class
      메서드와 속성을 캡슐화하는 코드 단위 |
      | Pointcut
      어드바이스가 실행되는 진입점들을 정의 | Method Signature
      비즈니스 로직 관심사의 구현 |
      | Advice
      횡단 관심사의 구현 | Method bodies
      비즈니스 로직 관심사의 구현 |
      | Weaver
      어드바이스로 코드(소스 또는 개체)를 구성 | Compiler
      소스 코드를 객체 코드로 변경 |

   5. 의존성을 추가

      build.gradle 파일에 의존성 추가

      ```java
      implementation 'org.springframework.boot:spring-boot-starter-aop'
      ```

   6. SpringBoot Application 클래스에 어노테이션 추가

      ```java
      @SpringBootApplication
      @EnableJpaAuditing
      @EnableAspectJAutoProxy(proxyTargetClass = true)
      public class Reviewapp0116Application {

      	public static void main(String[] args) {
      		SpringApplication.run(Reviewapp0116Application.class, args);
      	}

      }
      ```

   7. AOP 적용

      - Domain 클래스 생성 - domain.Employee
        ```java
        @Builder
        @Data
        public class Employee {
            private String empId;
            private String firstName;
            private String secondName;
        }
        ```
      - Service 인터페이스를 생성 - service.EmployeeService
        ```java
        public interface EmployeeService {
            Employee createEmployee(String empId, String fname, String sname);
        }
        ```
      - ServiceImpl 클래스 생성 - service.EmployeeServiceImpl
        ```java
        @Service
        public class EmployeeServiceImpl implements EmployeeService{
            @Override
            public Employee createEmployee(String empId, String fname, String sname) {
                Employee emp = Employee.builder()
                        .empId(empId)
                        .firstName(fname)
                        .secondName(sname)
                        .build();
                return emp;
            }
        }
        ```
      - Controller 클래스를 생성하고 요청을 처리하는 메서드를 추가 - controller.EmployeeController

        ```java
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
        ```

      - 실행한 후 http://localhost/add/employee?empId=100&firstName=kim&secondName=gyumin
      - AOP 적용 클래스를 생성 - aop.EmployeeServiceAspect

        ```java
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
        ```

      - 애플리케이션을 다시 실행하고 브라우저에 이전 URL([http://localhost/add/employee?empId=100&firstName=kim&secondName=gyumin](http://localhost/add/employee?empId=100&firstName=kim&secondName=gyumin))을 다시 입력하고 콘솔 확인
