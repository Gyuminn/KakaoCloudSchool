# [Spring Boot] Reactive Programming

1. **특성**
   - 비동기 이벤트 주도 프로그래밍
   - 함수형 프로그래밍 스타일
   - 스레드를 신경쓸 필요없는 동시성
   - 기존 자원을 더 효율적이고 일관성있게 사용하는 해법 중의 하나가 Reactive Stream
2. **Reactive Type**

   - Flux<데이터 자료형>
   - Java에서 Thread를 만들 때 Callable을 이용하면 Future<T> 으로 리턴되는데 이 타입은 스레드는 시작이 되었고 미래에서 데이터를 리턴받겠다는 의미인데 Flux는 시작할 준비가 되었다고 표현
   - non-blocking 방식으로 동작하기 때문에 작업을 수행 중에 종료될 때까지 기다리지 않음.
   - Flux의 특징
     - 하나 이상의 데이터를 포함시킬 수 있음.
     - 데이터가 제공될 때 어떤 일이 발생하는지 지정 가능
     - 성공과 실패 두 가지 경로 모두에 대한 처리 방향 정의 가능
     - 결과 폴링 불필요
     - 함수형 프로그래밍 지원
   - Dish 3개를 생성해서 리턴하는 메서드

     - 일반적인 경우
       ```java
       List<Dish> getDishes() {
       	List<DIsh> list = new ArrayList<>();
       	list.add(new Dish());
       	list.add(new Dish());
       	list.add(new Dish());
       	return
       ```
     - Flux를 이용할 때

       ```java
       Flux<Dish> getDishes() {
       	return Flux.just(new Dish(), new Dish(), new Dish());

       ```

     - 데이터를 받으면 doOnNext 또는 doOnError, doOnComplete와 같은 메서드를 체인 방식으로 호출해서 다음 작업을 수행할 수 있음.

3. **Reactive Applicatoin 생성**

   1. 프로젝트 생성 - springreactive0329

      lombok, Spring Reacitve Web, Dev Tools, Thyemleaf

   2. DTO 클래스 생성 - dto.Dish

      ```java
      package com.gyumin.springreactive0329.dto;

      import lombok.Data;

      @Data
      public class Dish {
          private String description;
          private boolean deliverd = false;

          public Dish(String description) {
              this.description = description;
          }
          public static Dish deliver(Dish dish) {
              Dish deliverdDish = new Dish(dish.description);
              deliverdDish.deliverd = true;
              return deliverdDish;
          }
      }
      ```

   3. service 클래스 생성 - service.KitchenService

      ```java
      package com.gyumin.springreactive0329.service;

      import com.gyumin.springreactive0329.dto.Dish;
      import org.springframework.stereotype.Service;
      import reactor.core.publisher.Flux;

      import java.time.Duration;
      import java.util.Arrays;
      import java.util.List;
      import java.util.Random;

      import static reactor.core.publisher.Flux.generate;

      @Service
      public class KitchenService {
          private Random random = new Random();

          private List<Dish> menu = Arrays.asList(
                  new Dish("치킨"),
                  new Dish("스파게티"),
                  new Dish("티본 스테이크")
          );

          private Dish randomDish() {
              return menu.get(random.nextInt(menu.size()));
          }

          public Flux<Dish> getDishes() {
              // 0.25초마다 데이터를 생성해서 리턴
              return Flux.<Dish> generate(sink -> sink.next(randomDish()))
                      .delayElements(Duration.ofMillis(250));
          }
      }
      ```

   4. controller todtjd - controller.ServerController

      ```java
      package com.gyumin.springreactive0329.controller;

      import com.gyumin.springreactive0329.dto.Dish;
      import com.gyumin.springreactive0329.service.KitchenService;
      import lombok.RequiredArgsConstructor;
      import org.springframework.web.bind.annotation.GetMapping;
      import org.springframework.web.bind.annotation.RestController;
      import reactor.core.publisher.Flux;

      @RestController
      @RequiredArgsConstructor
      public class ServerController {
          private final KitchenService kitchenService;

          @GetMapping(value = "/server", produces = "application/stream+json")
          Flux<Dish> serverDished() {
              return kitchenService.getDishes();
          }

      }
      ```

   5. 실행하고 브라우저에서 [localhost:8080/server로](http://localhost:8080/server로) 접속을 하면 주기적으로 계속 데이터를 전송하는 것을 확인할 수 있음.
