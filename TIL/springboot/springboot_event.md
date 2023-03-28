# [Spring Boot] Event

1. **개요**

   - 이벤트 메시지를 메시지를 게시하고 구독할 수 있는 메커니즘
     클래스와 어노테이션으로 기능 제공
   - 외부 메시지 브로커(Message Broker)
     프로세스와 프로세스 또는 인스턴스와 인스턴스 사이에 이벤트 메시지를 게시하고 구독하는 목적으로 사용
     메시지 브로커가 중간에서 이벤트 메시지를 전달하는 Queue 역할을 수행하기 때문에 프로세스와 프로세스 사이에 데이터를 주고받을 수 있고 신뢰성을 보장하기 때문에 MSA 환경에서는 한 컴포넌트에 서 다른 여러 컴포넌트에 데이터를 전파하는 목적으로 사용하거나 메시지 큐에 메시지를 쌓아두고 주기적으로 배치 프로세싱하는데 사용
     RabbitMQ나 ActiveMQ
     일반적인 RestTemplate, WebClient, HttpInterface와 같은 것만 이용했을 떄는 데이터를 제공하는 서비스에 이벤트가 발생했을 떄나 실시간으로 끊이지 않는 서비스를 사용할 떄 할 수 없다.
     **기본 개념은 변화가 생기면 알려주겠다! 라는 것임.**
     **서비스와 서비스 사이에는 → RabbitMQ나 ActiveMQ**
     내부에서 객체와 객체 사이 또는 스레드와 스레드 사이에 게시와 구독을 하는 서비스
   - **Spring Event -** 하나의 서비스(스프링부트) 안에서 사용하고 싶을 떄
     내부에서 객체와 객체 사이 또는 스레드와 스레드 사이에 게시와 구독을 하는 서비스
     이전에 했던 Web Push(Server Sent Events)를 구현할 때 이전의 방식을 이용해도 되고 Spring Event나 Reactive Programming(Web Flux + Netty)를 이용해도 된다.
   - 장점
     이벤트를 게시하는 클래스와 이벤트를 구독하는 클래스의 의존 관계 분리
     이벤트를 게시하고 구독하는 클래스를 비동기로 처리
     게시된 하나의 이벤트 메시지를 여러 개의 클래스가 수신 가능
     스프링 이벤트를 이용해서 트랜잭션을 효율적으로 사용
   - 시나리오
     회원 가입 기능 구현
     신규로 가입하면 포인트를 이메일로 발송해주는 이벤트를 수행하기로 결정

     ```java
     @Transactional
     회원가입() {
     절차
     이메일 발송하는 로직 // 여기다가 넣어버리는 것이 굉장히 안좋다!!!!!!!!!!!!!!!!!!!!
     }
     ```

     기존에 잘 동작하는 로직에 새로운 로직을 추가하는 것은 위험
     트랜잭션의 크기가 너무 커짐

     ```java
     @Transactional
     회원가입() {
     절차
     이메일 발송
     }

     @Tranㅗ젝
     ```

     첫 번째는 해결됐지만 두 번쨰는 여전히 같다.

     ```java
     @Transactional
     회원 가입() {
     절차
     메시지를 전송
     }

     회원가입에서 메시지를 전송하면 이메일 발송을 호출
     // 만약 이벤트가 끝나면 더 이상 구독하지 않도록 바꾸면 됨.

     @Transactional
     이메일 발송() {

     }
     ```

   1. 필요한 클래스

      - Event Message
      - Publisher
      - Listener

      **게시자 → 이벤트 메시지 → Spring Application Context → 이벤트 메시지 → 구독자**

2. **프로젝트 실습**

   1. 기본 구성으로 진행

      Lombok, Spring Web, dev tools

   2. dto.UserEvent

      ```java
      package com.gyumin.springevent0323.dto;

      import lombok.Getter;
      import org.springframework.context.ApplicationEvent;

      @Getter
      public class UserEvent extends ApplicationEvent {
          // 회원가입과 탈퇴를 구분하기 위한 속성
          private Type type;
          private Long userId;
          private String emailAddress;

          // 보통 이렇게 안했었음.
          // 메시지를 위해 상속받으려고 해보니 super() 가 에러난다.
          // 이런 경우 상위 클래스에 ~~ 한 것이다.
          private UserEvent(Object source, Type type, Long userId, String emailAddress) {
              super(source);
              this.type = type;
              this.userId = userId;
              this.emailAddress = emailAddress;
          }

          // 위에서 생성자를 private으로 만들었기 때문에
          // 외부에서 생성자를 만들 수 있도록 하는 메서드를 만들어야 함.
          // 인스턴스를 생성해주는 별도의 static 메서드 생성
          // 싱글톤 패턴을 적용할 때 또는 생성하는 코드가 복잡할 때 주로 이용
          public static UserEvent created(Object source, Long userId, String emailAddress) {
              return new UserEvent(source, Type.CREATE, userId, emailAddress);
          }
          public enum Type{
              CREATE, DELETE
          }
      }
      ```

   3. 게시자 클래스

      POJO(다른 프레임워크의 클래스를 상속하지 않은 클래스의 클래스)로 생성. 반대는 (DOSO - Dependency - POJO와 다르게 다른 크레임워크의 클래스를 상속받은 클래스)

      service.UserEventPublisher

      ```java
      package com.gyumin.springevent0323.service;

      import com.gyumin.springevent0323.dto.UserEvent;
      import org.springframework.context.ApplicationEventPublisher;
      import org.springframework.stereotype.Component;

      @Component
      public class UserEventPublisher {
          private final ApplicationEventPublisher applicationEventPublisher;

          public UserEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
              this.applicationEventPublisher = applicationEventPublisher;
          }

          // 게시하는 메서드
          public void publishUserCreated(Long userId, String emailAddress) {
              // 메시지 생성
              UserEvent userEvent = UserEvent.created(this, userId, emailAddress);
              System.out.println("게시");

              // 이벤트 게시
              applicationEventPublisher.publishEvent(userEvent);
          }
      }
      ```

   4. 구독자 클래스 - service.UserEventListener

      ApplicationListener<메시지 타입>을 implements

      ```java
      package com.gyumin.springevent0323.service;

      import com.gyumin.springevent0323.dto.UserEvent;
      import org.springframework.context.ApplicationListener;
      import org.springframework.stereotype.Component;

      @Component
      public class UserEventListener implements ApplicationListener<UserEvent> {

          // 이벤트가 게시되었을 때 호출될 메서드
          @Override
          public void onApplicationEvent(UserEvent event) {
              if (event.getType() == UserEvent.Type.CREATE) {
                  System.out.println("생성 이벤트를 구독함");
                  // 수행할 작업
              }
          }
      }
      ```

   5. 서비스 클래스 - service.UserService

      ```java
      package com.gyumin.springevent0323.service;

      import lombok.RequiredArgsConstructor;
      import org.springframework.stereotype.Service;

      @Service
      @RequiredArgsConstructor
      public class UserService {
          // 게시자 클래스 주입
          private final UserEventPublisher userEventPublisher;

          public Boolean createUser(String userName, String emailAddress) {
              System.out.println("회원가입");
              // 메시지를 게시
              userEventPublisher.publishUserCreated(1234567890L, emailAddress);
              return true;
          }
      }
      ```

   6. Applicatoin 클래스의 main 클래스에 추가

      ```java
      package com.gyumin.springevent0323;

      import com.gyumin.springevent0323.service.UserService;
      import org.springframework.boot.SpringApplication;
      import org.springframework.boot.autoconfigure.SpringBootApplication;
      import org.springframework.boot.builder.SpringApplicationBuilder;
      import org.springframework.context.ConfigurableApplicationContext;

      @SpringBootApplication
      public class Springevent0323Application {

          public static void main(String[] args) {
              // Application context 찾아오기
              SpringApplicationBuilder appBuilder = new SpringApplicationBuilder(Springevent0323Application.class);
              SpringApplication application = appBuilder.build();
              ConfigurableApplicationContext context = application.run(args);

              // Bean 찾아오기
              // 서비스 메서드 호출
              UserService userService = context.getBean(UserService.class);
              userService.createUser("kimgyumin", "gyumin@kakao.com");
          }

      }
      ```

   7. 현재 상태에서 실행

      **하나의 스레드에서 모두 동작 - 동기적으로 동작한다는 것임**

   8. 멀티 스레드 적용 - 설정 클래스만 추가하면 됨. → 비동기적으로 처리

      config.AsyncEventConfig

      ```java
      package com.gyumin.springevent0323.config;

      import org.springframework.context.annotation.Bean;
      import org.springframework.context.annotation.Configuration;
      import org.springframework.context.event.ApplicationEventMulticaster;
      import org.springframework.context.event.SimpleApplicationEventMulticaster;
      import org.springframework.core.task.TaskExecutor;
      import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

      import java.util.concurrent.ThreadPoolExecutor;

      // 멀티 스레드로 이벤트 처리를 수행하도록 해주는 설정 클래스
      @Configuration
      public class AsyncEventConfig {
          @Bean
          public ApplicationEventMulticaster applicationEventMulticaster(TaskExecutor asyncEventTaskExecutor) {
              SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();
              eventMulticaster.setTaskExecutor(asyncEventTaskExecutor);
              return eventMulticaster;
          }
          // 스레드 풀을 사용하기 위한 빈
          @Bean
          public TaskExecutor asyncEventTaskExecutor() {
              ThreadPoolTaskExecutor asyncEventTaskExecutor = new ThreadPoolTaskExecutor();
              asyncEventTaskExecutor.setMaxPoolSize(10);
              asyncEventTaskExecutor.setThreadNamePrefix("eventExecutor");
              asyncEventTaskExecutor.afterPropertiesSet();
              return asyncEventTaskExecutor;
          }
      }
      ```

   9. **Spring에서는 @Async 어노테이션을 지원**

      클래스 위에 붙이면 클래스의 모든 public 메서드가 별도의 스레드에서 동작

      메서드 위에 붙이면 메서드만 별도의 스레드에서 동작

      **이렇게 하면 편한데 스레드가 몇 개 만들어지는지 알지 못함. 커스터마이징이 안된다.**

3. **Spring Application Event**

   1. 개요
      - 스프링 애플리케이션이 시작하여 실행 가능한 상태까지 발생할 수 있는 여러 이벤트를 정의하고 게시하는 기능을 제공
      - 구독하는 클래스를 만들 때 @EventListener라는 어노테이션을 이용할 수 있고 ApplicationListener 인터페이스를 구현해서 생성할 수 있는데 **직접 구현하는 것을 권장**
   2. 이벤트
      - ApplicationStartingEvent
        애플리케이션이 시작될 때 발생하는 이벤트로 스프링 빈 스캔 및 로딩같은 작업도 시작하지 않은 상태
      - ApplicationEnvironmentPreparedEvent
        애플리케이션이 사용하는 환경 설정 객체가 준비된 시점에 발생하는 이벤트
      - ApplicationStartedEvent
        애플리케이션이 시작된 후 발생하는 이벤트로 애플리케이션과 커맨드 라인 러너가 호출되기 전
      - ApplicationReadyEvent
        이름 때문에 먼저 실행된다고 생각할 수 있지만 모든 애플리케이션이 실행 된 후이다.
      - **ApplicationFailedEvent**
        애플리케이션이 정상으로 실행되지 못할 때 발생하는 이벤트
        **마이크로 서비스에서 중요**
        서비스가 엉켜있을 때 메시지를 보내주는건 굉장히 중요
      - 클라이언트 앱 ↔ Application Server ↔ API Server ↔ 데이터 저장소
        클라이언트 앱 ↔ Application Server ↔ AWS 데이터 저장소
   3. 구독자 클래스를 만드는 방법
      - ApplicationListener<이벤트이름>을 구현한 클래스를 만들고 onApplicationEvent 메서드를 재정의해서 이벤트가 발생했을 때 수행할 작업을 생성
   4. 트랜잭션 시점에 이벤트 구도 가능

      - 트랜잭션을 커밋하기 직전
      - 트랜잭션을 커밋한 후
      - 트랜잭션이 롤백된 후
      - 트랜잭션이 커밋이나 롤백된 후

      관계형 데이터베이스에서는 이 작업을 Trigger로 작업
